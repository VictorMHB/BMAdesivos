package com.github.victormhb.bmadesivos.service;

import com.github.victormhb.bmadesivos.dto.insumo.InsumoDTO;
import com.github.victormhb.bmadesivos.dto.insumo.InsumoUpdateDTO;
import com.github.victormhb.bmadesivos.entity.Insumo;
import com.github.victormhb.bmadesivos.repository.InsumoRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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

        Insumo insumo = new Insumo();
        insumo.setNome(dto.nome());
        insumo.setUnidadeMedida(dto.unidadeMedida());
        insumo.setEstoqueAtual(dto.estoqueAtual() != null ? dto.estoqueAtual() : 0.0);
        insumo.setEstoqueMinimo(dto.estoqueMinimo() != null ? dto.estoqueMinimo() : 0.0);
        insumo.setValorUnitario(dto.valorUnitario() != null ? dto.valorUnitario() : 0.0);
        insumo.setAtivo(true);

        return insumoRepository.save(insumo);
    }

    @Transactional
    public Insumo atualizarInsumo(Long id, InsumoUpdateDTO dto) throws Exception {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new Exception("Material com ID " + id + " não foi encontrado"));

        if (dto.getNome() != null && !dto.getNome().trim().isEmpty()) {
            insumo.setNome(dto.getNome());
        }
        if (dto.getUnidadeMedida() != null) {
            insumo.setUnidadeMedida(dto.getUnidadeMedida());
        }
        if (dto.getEstoqueAtual() != null) {
            insumo.setEstoqueAtual(dto.getEstoqueAtual());
        }
        if (dto.getEstoqueMinimo() != null) {
            insumo.setEstoqueMinimo(dto.getEstoqueMinimo());
        }
        if (dto.getValorUnitario() != null) {
            insumo.setValorUnitario(dto.getValorUnitario());
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

        if (insumo.getEstoqueAtual() <= insumo.getEstoqueMinimo()) {
            System.out.println("ALERTA: " + insumo.getNome() + " atingiu o nível crítico.");
        }
    }

    @Transactional
    public void deletarInsumo(Long id) throws Exception {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new Exception("Material não encontrado"));

        insumo.setAtivo(false);
        insumoRepository.save(insumo);
    }
}
