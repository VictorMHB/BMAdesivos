package com.github.victormhb.bmadesivos.service;

import com.github.victormhb.bmadesivos.dto.adesivo.AdesivoDTO;
import com.github.victormhb.bmadesivos.dto.adesivo.AdesivoUpdateDTO;
import com.github.victormhb.bmadesivos.entity.Adesivo;
import com.github.victormhb.bmadesivos.entity.Cliente;
import com.github.victormhb.bmadesivos.repository.AdesivoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdesivoService {

    private final AdesivoRepository adesivoRepository;
    private final ClienteService clienteService;

    @Autowired
    public AdesivoService(AdesivoRepository adesivoRepository, ClienteService clienteService) {
        this.adesivoRepository = adesivoRepository;
        this.clienteService = clienteService;
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

        if (dto.precoVenda() == null || dto.precoVenda() <= 0) {
            throw new Exception("O preço de venda deve ser maior que zero.");
        }

        if (dto.clienteId() == null) {
            throw new Exception("O cliente é obrigatório.");
        }

        Cliente cliente = clienteService.buscarPorId(dto.clienteId());

        Adesivo adesivo = new Adesivo();
        adesivo.setNome(nomeTratado);
        adesivo.setDescricao(dto.descricao() != null ? dto.descricao().trim() : null);
        adesivo.setTipoAdesivo(dto.tipoAdesivo());
        adesivo.setComprimento(dto.comprimento());
        adesivo.setAltura(dto.altura());
        adesivo.setValorUnitario(dto.precoVenda());
        adesivo.setCliente(cliente);
        adesivo.setAtivo(true);

        return adesivoRepository.save(adesivo);
    }

    @Transactional
    public Adesivo atualizarAdesivo(Long id, AdesivoUpdateDTO dto) throws Exception {
        Adesivo adesivo = buscarPorId(id);

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
            adesivo.setComprimento(dto.getComprimento());
        }

        if (dto.getAltura() != null) {
            adesivo.setAltura(dto.getAltura());
        }

        if (dto.getPrecoVenda() != null && dto.getPrecoVenda() > 0) {
            adesivo.setValorUnitario(dto.getPrecoVenda());
        }

        if (dto.getAtivo() != null) {
            adesivo.setAtivo(dto.getAtivo());
        }

        return adesivoRepository.save(adesivo);
    }

    @Transactional
    public void deletarAdesivo(Long id) throws Exception {
        Adesivo adesivo = buscarPorId(id);
        adesivo.setAtivo(false);
        adesivoRepository.save(adesivo);
    }
}