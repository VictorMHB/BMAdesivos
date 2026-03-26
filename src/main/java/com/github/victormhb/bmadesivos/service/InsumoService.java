package com.github.victormhb.bmadesivos.service;

import com.github.victormhb.bmadesivos.dto.insumo.InsumoDTO;
import com.github.victormhb.bmadesivos.dto.insumo.InsumoUpdateDTO;
import com.github.victormhb.bmadesivos.entity.Insumo;
import com.github.victormhb.bmadesivos.enums.TipoInsumo;
import com.github.victormhb.bmadesivos.repository.InsumoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InsumoService {

    private final InsumoRepository insumoRepository;

    @Autowired
    public InsumoService(InsumoRepository insumoRepository) {
        this.insumoRepository = insumoRepository;
    }

    public List<Insumo> listar() {
        return insumoRepository.findAll(Sort.by(Sort.Direction.ASC, "nome"));
    }

    public Insumo buscarPorId(Long id) throws Exception {
        return insumoRepository.findById(id)
                .orElseThrow(() -> new Exception("Material com ID: " + id + " não foi encontrado"));
    }

    @Transactional
    public Insumo adicionarInsumo(InsumoDTO dto) throws Exception {
        if (dto.nome() == null || dto.nome().trim().isEmpty()) {
            throw new Exception("O nome do material é obrigatório.");
        }

        String nomeTratado = dto.nome().trim();

        if (nomeTratado.length() < 3) {
            throw new Exception("Nome deve ter no mínimo 3 caracteres.");
        }

        if (!nomeTratado.matches("[\\p{L}0-9 ]+")) {
            throw new Exception("Nome contém caracteres inválidos.");
        }

        if (dto.tipoInsumo() == null) {
            throw new Exception("O tipo do insumo é obrigatório.");
        }

        if (dto.estoqueAtual() == null || dto.estoqueAtual() < 0) {
            throw new Exception("Estoque atual inválido.");
        }

        Insumo insumo = new Insumo();
        insumo.setNome(nomeTratado);
        insumo.setDescricao(dto.descricao() != null ? dto.descricao().trim() : null);
        insumo.setTipoInsumo(dto.tipoInsumo());
        insumo.setEstoqueAtual(dto.estoqueAtual());
        insumo.setValorUnitario(dto.valorUnitario());
        insumo.setAtivo(true);

        // Substrato
        if (dto.tipoInsumo() == TipoInsumo.SUBSTRATO) {
            insumo.setLargura(dto.largura());
            insumo.setComprimento(dto.comprimento());
            insumo.setQuantidadeRolos(dto.quantidadeRolos() != null ? dto.quantidadeRolos() : 0);

            Double m2PorRolo = null;
            if (dto.largura() != null && dto.comprimento() != null) {
                m2PorRolo = dto.largura() * dto.comprimento();
                insumo.setMetrosQuadrados(m2PorRolo);
            } else if (dto.metrosQuadrados() != null) {
                m2PorRolo = dto.metrosQuadrados();
                insumo.setMetrosQuadrados(m2PorRolo);
            }

            if (m2PorRolo != null && dto.quantidadeRolos() != null) {
                insumo.setEstoqueAtual(m2PorRolo * dto.quantidadeRolos());
            } else {
                insumo.setEstoqueAtual(dto.estoqueAtual() != null ? dto.estoqueAtual() : 0.0);
            }
        }

        // Tinta
        if (dto.tipoInsumo() == TipoInsumo.TINTA) {
            if (dto.cor() == null || dto.cor().trim().isEmpty()) {
                throw new Exception("A cor é obrigatória para tintas.");
            }
            if (dto.tamanhoEmbalagem() == null) {
                throw new Exception("O tamanho da embalagem é obrigatório para tintas.");
            }
            insumo.setCor(dto.cor().trim());
            insumo.setTamanhoEmbalagem(dto.tamanhoEmbalagem());
        }

        return insumoRepository.save(insumo);
    }

    @Transactional
    public Insumo atualizarInsumo(Long id, InsumoUpdateDTO dto) throws Exception {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new Exception("Material com ID " + id + " não foi encontrado"));

        if (dto.getNome() != null && !dto.getNome().trim().isEmpty()) {
            String nomeTratado = dto.getNome().trim();

            if (nomeTratado.length() < 3) {
                throw new Exception("Nome deve ter no mínimo 3 caracteres.");
            }

            if (!nomeTratado.matches("[\\p{L}0-9 ]+")) {
                throw new Exception("Nome contém caracteres inválidos.");
            }

            insumo.setNome(nomeTratado);
        }

        if (dto.getDescricao() != null) {
            insumo.setDescricao(dto.getDescricao().trim());
        }

        if (dto.getEstoqueAtual() != null) {
            insumo.setEstoqueAtual(dto.getEstoqueAtual());
        }

        if (dto.getValorUnitario() != null) {
            insumo.setValorUnitario(dto.getValorUnitario());
        }

        if (dto.getTipoInsumo() != null) {
            insumo.setTipoInsumo(dto.getTipoInsumo());
        }

        // Substrato
        if (insumo.getTipoInsumo() == TipoInsumo.SUBSTRATO) {
            if (dto.getLargura() != null) insumo.setLargura(dto.getLargura());
            if (dto.getComprimento() != null) insumo.setComprimento(dto.getComprimento());
            if (dto.getQuantidadeRolos() != null) insumo.setQuantidadeRolos(dto.getQuantidadeRolos());

            Double m2PorRolo = null;
            if (insumo.getLargura() != null && insumo.getComprimento() != null) {
                m2PorRolo = insumo.getLargura() * insumo.getComprimento();
                insumo.setMetrosQuadrados(m2PorRolo);
            } else if (dto.getMetrosQuadrados() != null) {
                m2PorRolo = dto.getMetrosQuadrados();
                insumo.setMetrosQuadrados(m2PorRolo);
            }

            if (m2PorRolo != null && insumo.getQuantidadeRolos() != null) {
                insumo.setEstoqueAtual(m2PorRolo * insumo.getQuantidadeRolos());
            }
        }

        // Tinta
        if (insumo.getTipoInsumo() == TipoInsumo.TINTA) {
            if (dto.getCor() != null) insumo.setCor(dto.getCor().trim());
            if (dto.getTamanhoEmbalagem() != null) insumo.setTamanhoEmbalagem(dto.getTamanhoEmbalagem());
        }

        if (dto.getAtivo() != null) {
            insumo.setAtivo(dto.getAtivo());
        }

        return insumoRepository.save(insumo);
    }

    @Transactional
    public void baixarEstoque(Long id, Double qtdConsumida) throws Exception {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new Exception("Material não encontrado"));

        if (insumo.getEstoqueAtual() < qtdConsumida) {
            throw new Exception("Estoque insuficiente de " + insumo.getNome());
        }

        insumo.setEstoqueAtual(insumo.getEstoqueAtual() - qtdConsumida);
        insumoRepository.save(insumo);
    }

    @Transactional
    public void deletarInsumo(Long id) throws Exception {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new Exception("Material não encontrado"));

        insumo.setAtivo(false);
        insumoRepository.save(insumo);
    }
}