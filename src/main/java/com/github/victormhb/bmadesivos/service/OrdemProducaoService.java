package com.github.victormhb.bmadesivos.service;

import com.github.victormhb.bmadesivos.dto.ordem.ItemOrdemDTO;
import com.github.victormhb.bmadesivos.dto.ordem.OrdemProducaoDTO;
import com.github.victormhb.bmadesivos.entity.*;
import com.github.victormhb.bmadesivos.enums.StatusOrdem;
import com.github.victormhb.bmadesivos.enums.TipoAdesivo;
import com.github.victormhb.bmadesivos.enums.TipoInsumo;
import com.github.victormhb.bmadesivos.repository.ClienteRepository;
import com.github.victormhb.bmadesivos.repository.FuncionarioRepository;
import com.github.victormhb.bmadesivos.repository.OrdemProducaoRepository;
import com.github.victormhb.bmadesivos.repository.FichaTecnicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrdemProducaoService {

    private final OrdemProducaoRepository ordemProducaoRepository;
    private final AdesivoService adesivoService;
    private final FuncionarioRepository funcionarioRepository;
    private final InsumoService insumoService;
    private final FichaTecnicaRepository fichaTecnicaRepository;
    private final ClienteRepository clienteRepository;

    @Autowired
    public OrdemProducaoService(OrdemProducaoRepository ordemProducaoRepository,
                                AdesivoService adesivoService,
                                FuncionarioRepository funcionarioRepository,
                                InsumoService insumoService,
                                FichaTecnicaRepository fichaTecnicaRepository,
                                ClienteRepository clienteRepository) {
        this.ordemProducaoRepository = ordemProducaoRepository;
        this.adesivoService = adesivoService;
        this.funcionarioRepository = funcionarioRepository;
        this.insumoService = insumoService;
        this.fichaTecnicaRepository = fichaTecnicaRepository;
        this.clienteRepository = clienteRepository;
    }

    public List<OrdemProducao> listar() {
        return ordemProducaoRepository.findByArquivadaFalseAndStatusNot(StatusOrdem.CANCELADO,
                Sort.by(Sort.Direction.DESC, "dataAbertura"));
    }

    public List<OrdemProducao> listarHistorico() {
        return ordemProducaoRepository.findByArquivadaTrueOrStatus(StatusOrdem.CANCELADO,
                Sort.by(Sort.Direction.DESC, "dataAbertura"));
    }

    public OrdemProducao buscarPorId(Long id) throws Exception {
        return ordemProducaoRepository.findById(id)
                .orElseThrow(() -> new Exception("Ordem de produção não encontrada."));
    }

    @Transactional
    public OrdemProducao abrirOrdem(OrdemProducaoDTO dto) throws Exception {
        if (dto.funcionarioId() == null)
            throw new Exception("Funcionário é obrigatório.");
        if (dto.clienteId() == null)
            throw new Exception("Cliente é obrigatório.");
        if (dto.itens() == null || dto.itens().isEmpty())
            throw new Exception("A ordem deve ter pelo menos um adesivo.");

        Funcionario funcionario = funcionarioRepository.findById(dto.funcionarioId())
                .orElseThrow(() -> new Exception("Funcionário não encontrado."));

        Cliente cliente = clienteRepository.findById(dto.clienteId())
                .orElseThrow(() -> new Exception("Cliente não encontrado."));

        OrdemProducao ordem = new OrdemProducao();
        ordem.setFuncionario(funcionario);
        ordem.setCliente(cliente);
        ordem.setStatus(StatusOrdem.PENDENTE);
        ordem.setDataAbertura(LocalDateTime.now());
        ordem.setDataPrazo(dto.dataPrazo());
        ordem.setAtivo(true);

        if (dto.dataPrazo() != null && dto.dataPrazo().isBefore(LocalDateTime.now()))
            throw new Exception("O prazo não pode ser uma data no passado.");

        for (ItemOrdemDTO itemDTO : dto.itens()) {
            if (itemDTO.quantidade() == null || itemDTO.quantidade() <= 0)
                throw new Exception("Quantidade de cada item deve ser maior que zero.");

            Adesivo adesivo = adesivoService.buscarPorId(itemDTO.adesivoId());

            if (!adesivo.getCliente().getId().equals(cliente.getId()))
                throw new Exception("O adesivo '" + adesivo.getNome() + "' não pertence ao cliente selecionado.");

            ItemOrdem item = new ItemOrdem();
            item.setOrdem(ordem);
            item.setAdesivo(adesivo);
            item.setQuantidade(itemDTO.quantidade());
            ordem.getItens().add(item);
        }

        return ordemProducaoRepository.save(ordem);
    }

    @Transactional
    public OrdemProducao avancarStatus(Long id) throws Exception {
        OrdemProducao ordem = buscarPorId(id);

        if (ordem.getStatus() == StatusOrdem.CONCLUIDO)
            throw new Exception("Ordem já concluída.");
        if (ordem.getStatus() == StatusOrdem.CANCELADO)
            throw new Exception("Ordem cancelada não pode ser avançada.");

        if (ordem.getStatus() == StatusOrdem.PENDENTE) {
            ordem.setStatus(StatusOrdem.EM_PRODUCAO);
        } else if (ordem.getStatus() == StatusOrdem.EM_PRODUCAO) {
            ordem.setStatus(StatusOrdem.CONCLUIDO);
            ordem.setDataConclusao(LocalDateTime.now());
        }

        return ordemProducaoRepository.save(ordem);
    }

    @Transactional
    public OrdemProducao finalizarOrdem(Long id) throws Exception{
        OrdemProducao ordem = buscarPorId(id);

        if (ordem.getStatus() == StatusOrdem.CONCLUIDO)
            throw new Exception("Ordem já foi finalizada.");
        if (ordem.getStatus() == StatusOrdem.CANCELADO)
            throw new Exception("Ordem cancelada não pode ser finalizada.");

        for (ItemOrdem item : ordem.getItens()) {
            Adesivo adesivo = item.getAdesivo();
            TipoAdesivo tipo = adesivo.getTipoAdesivo();

            if (tipo == TipoAdesivo.ETIQUETA_METALICA) continue;

            if (adesivo.getAreaCm2() == null)
                throw new Exception("Adesivo '" + adesivo.getNome() + "' não possui área cadastrada.");

            List<FichaTecnica> fichas = fichaTecnicaRepository.findByAdesivoAndAtivoTrue(adesivo);

            if (fichas.isEmpty())
                throw new Exception("Adesivo '" + adesivo.getNome() + "' não possui ficha técnica cadastrada.");

            double areaCm2 = adesivo.getAreaCm2();

            for (FichaTecnica ficha : fichas) {
                Insumo insumo = ficha.getInsumo();

                if (insumo.getTipoInsumo() == TipoInsumo.SUBSTRATO) {
                    double consumo = (areaCm2 / 10_000.0) * item.getQuantidade();
                    insumoService.baixarEstoque(insumo.getId(), consumo);
                }

                if (insumo.getTipoInsumo() == TipoInsumo.RESINA) {
                    double consumoGramas = areaCm2 * 0.20 * item.getQuantidade();
                    double consumoKg = consumoGramas / 1000.0;
                    insumoService.baixarEstoque(insumo.getId(), consumoKg);
                }
            }
        }

        ordem.setStatus(StatusOrdem.CONCLUIDO);
        ordem.setDataConclusao(LocalDateTime.now());
        return ordemProducaoRepository.save(ordem);
    }

    @Transactional
    public OrdemProducao arquivarOrdem(Long id) throws Exception {
        OrdemProducao ordem = buscarPorId(id);
        if (ordem.getStatus() != StatusOrdem.CONCLUIDO) {
            throw new Exception("Apenas ordens concluídas podem ser arquivadas.");
        }
        ordem.setArquivada(true);
        return ordemProducaoRepository.save(ordem);
    }

    @Transactional
    public OrdemProducao cancelarOrdem(Long id) throws Exception {
        OrdemProducao ordem = buscarPorId(id);

        if (ordem.getStatus() == StatusOrdem.CONCLUIDO)
            throw new Exception("Não é possível cancelar uma ordem já concluída.");

        ordem.setStatus(StatusOrdem.CANCELADO);
        return ordemProducaoRepository.save(ordem);
    }
}