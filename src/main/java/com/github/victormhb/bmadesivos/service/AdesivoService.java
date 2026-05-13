package com.github.victormhb.bmadesivos.service;

import com.github.victormhb.bmadesivos.dto.adesivo.AdesivoDTO;
import com.github.victormhb.bmadesivos.dto.adesivo.AdesivoUpdateDTO;
import com.github.victormhb.bmadesivos.entity.Adesivo;
import com.github.victormhb.bmadesivos.entity.Cliente;
import com.github.victormhb.bmadesivos.entity.FichaTecnica;
import com.github.victormhb.bmadesivos.entity.Insumo;
import com.github.victormhb.bmadesivos.enums.TipoAdesivo;
import com.github.victormhb.bmadesivos.enums.TipoInsumo;
import com.github.victormhb.bmadesivos.repository.AdesivoRepository;
import com.github.victormhb.bmadesivos.repository.FichaTecnicaRepository;
import com.github.victormhb.bmadesivos.repository.InsumoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdesivoService {

    private final AdesivoRepository adesivoRepository;
    private final ClienteService clienteService;
    private final InsumoRepository insumoRepository;
    private final FichaTecnicaRepository fichaTecnicaRepository;

    @Autowired
    public AdesivoService(AdesivoRepository adesivoRepository,
                          ClienteService clienteService,
                          InsumoRepository insumoRepository,
                          FichaTecnicaRepository fichaTecnicaRepository) {
        this.adesivoRepository = adesivoRepository;
        this.clienteService = clienteService;
        this.insumoRepository = insumoRepository;
        this.fichaTecnicaRepository = fichaTecnicaRepository;
    }

    public List<Adesivo> listar() {
        Sort ordenacao = Sort.by(Sort.Direction.DESC, "ativo")
                .and(Sort.by(Sort.Direction.ASC, "nome"));
        return adesivoRepository.findAll(ordenacao);
    }

    public Adesivo buscarPorId(Long id) throws Exception {
        return adesivoRepository.findById(id)
                .orElseThrow(() -> new Exception("Adesivo com ID: " + id + " não encontrado."));
    }

    @Transactional
    public Adesivo adicionarAdesivo(AdesivoDTO dto) throws Exception {
        if (dto.nome() == null || dto.nome().trim().isEmpty()) {
            throw new Exception("O nome do adesivo é obrigatório.");
        }

        String nomeTratado = dto.nome().trim();

        if (nomeTratado.length() < 3) {
            throw new Exception("Nome deve ter no mínimo 3 caracteres.");
        }

        if (!nomeTratado.matches("[\\p{L}0-9 ]+")) {
            throw new Exception("Nome contém caracteres inválidos.");
        }

        if (dto.tipoAdesivo() == null) {
            throw new Exception("O tipo do adesivo é obrigatório.");
        }

        if (dto.valorUnitario() == null || dto.valorUnitario() <= 0) {
            throw new Exception("O preço de venda deve ser maior que zero.");
        }

        if (dto.clienteId() == null) {
            throw new Exception("O cliente é obrigatório.");
        }

        if (dto.substratoId() == null) {
            throw new Exception("O substrato é obrigatório.");
        }

        if (dto.comprimento() == null || dto.altura() == null || dto.comprimento() <= 0 || dto.altura() <= 0) {
            throw new Exception("Comprimento e altura do adesivo são obrigatórios e devem ser maiores que zero.");
        }

        Cliente cliente = clienteService.buscarPorId(dto.clienteId());

        Adesivo adesivo = new Adesivo();
        adesivo.setNome(nomeTratado);
        adesivo.setDescricao(dto.descricao() != null ? dto.descricao().trim() : null);
        adesivo.setTipoAdesivo(dto.tipoAdesivo());
        adesivo.setComprimento(dto.comprimento());
        adesivo.setAltura(dto.altura());
        adesivo.setAreaCm2(dto.comprimento() * dto.altura());
        adesivo.setValorUnitario(dto.valorUnitario());
        adesivo.setCliente(cliente);
        adesivo.setAtivo(true);

        Adesivo salvo = adesivoRepository.save(adesivo);
        criarFichaTecnica(salvo, dto.substratoId(), dto.resinaId());

        return salvo;
    }

    @Transactional
    public Adesivo atualizarAdesivo(Long id, AdesivoUpdateDTO dto) throws Exception {
        Adesivo adesivo = buscarPorId(id);

        Double novoComprimento = dto.getComprimento() != null ? dto.getComprimento() : adesivo.getComprimento();
        Double novaAltura = dto.getAltura() != null ? dto.getAltura() : adesivo.getAltura();

        if (dto.getNome() != null && !dto.getNome().trim().isEmpty()) {
            String nomeTratado = dto.getNome().trim();

            if (nomeTratado.length() < 3) {
                throw new Exception("Nome deve ter no mínimo 3 caracteres.");
            }

            if (!nomeTratado.matches("[\\p{L}0-9 ]+")) {
                throw new Exception("Nome contém caracteres inválidos.");
            }

            adesivo.setNome(nomeTratado);
        }

        if (dto.getDescricao() != null) {
            adesivo.setDescricao(dto.getDescricao().trim());
        }

        if (dto.getTipoAdesivo() != null) {
            adesivo.setTipoAdesivo(dto.getTipoAdesivo());
        }

        if (dto.getComprimento() != null) {
            adesivo.setComprimento(novoComprimento);
        }
        if (dto.getAltura() != null) {
            adesivo.setAltura(novaAltura);
        }

        if (novoComprimento != null && novaAltura != null) {
            adesivo.setAreaCm2(novoComprimento * novaAltura); // ← recalcula
        }

        if (dto.getValorUnitario() != null && dto.getValorUnitario() > 0) {
            adesivo.setValorUnitario(dto.getValorUnitario());
        }

        if (dto.getAtivo() != null) {
            adesivo.setAtivo(dto.getAtivo());
        }

        if (dto.getClienteId() != null) {
            Cliente cliente = clienteService.buscarPorId(dto.getClienteId());
            adesivo.setCliente(cliente);
        }

        boolean atualizarFicha = dto.getSubstratoId() != null || dto.getResinaId() != null;

        if (atualizarFicha) {
            List<FichaTecnica> itensAntigos = fichaTecnicaRepository.findByAdesivoAndAtivoTrue(adesivo);
            itensAntigos.forEach(item -> item.setAtivo(false));
            fichaTecnicaRepository.saveAll(itensAntigos);
            criarFichaTecnica(adesivo, dto.getSubstratoId(), dto.getResinaId());
        }

        return adesivoRepository.save(adesivo);
    }

    private void criarFichaTecnica(Adesivo adesivo, Long substratoId, Long resinaId) throws Exception {
        if (substratoId != null) {
            Insumo substrato = insumoRepository.findById(substratoId)
                    .orElseThrow(() -> new Exception("Substrato não encontrado."));
            if (substrato.getTipoInsumo() != TipoInsumo.SUBSTRATO)
                throw new Exception("Insumo selecionado não é um substrato.");

            FichaTecnica itemSubstrato = new FichaTecnica();
            itemSubstrato.setAdesivo(adesivo);
            itemSubstrato.setInsumo(substrato);
            itemSubstrato.setAtivo(true);
            fichaTecnicaRepository.save(itemSubstrato);
        }

        if (resinaId != null) {
            Insumo resina = insumoRepository.findById(resinaId)
                    .orElseThrow(() -> new Exception("Resina não encontrada."));
            if (resina.getTipoInsumo() != TipoInsumo.RESINA)
                throw new Exception("Insumo selecionado não é uma resina.");

            FichaTecnica itemResina = new FichaTecnica();
            itemResina.setAdesivo(adesivo);
            itemResina.setInsumo(resina);
            itemResina.setAtivo(true);
            fichaTecnicaRepository.save(itemResina);
        }
    }

    @Transactional
    public void deletarAdesivo(Long id) throws Exception {
        Adesivo adesivo = buscarPorId(id);
        adesivo.setAtivo(false);
        adesivoRepository.save(adesivo);
    }
}