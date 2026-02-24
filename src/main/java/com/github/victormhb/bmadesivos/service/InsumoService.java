package com.github.victormhb.bmadesivos.service;

import com.github.victormhb.bmadesivos.dto.InsumoDTO;
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

    public List<Insumo> listarTodos() {
        return insumoRepository.findAll(Sort.by(Sort.Direction.ASC, "nome"));
    }

    public Insumo buscarPorId(Long id) throws Exception {
        return insumoRepository.findById(id)
                .orElseThrow(() -> new Exception("Material com ID: " + id + " não foi encontrado"));
    }

    @Transactional
    public Insumo salvar(InsumoDTO dto) throws Exception {
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
    public Insumo atualizar(Long id, InsumoDTO dto) throws Exception {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new Exception("Material com ID " + id + " não foi encontrado"));

        if (dto.nome() != null && !dto.nome().trim().isEmpty()) {
            insumo.setNome(dto.nome());
        }
        if (dto.unidadeMedida() != null) {
            insumo.setUnidadeMedida(dto.unidadeMedida());
        }
        if (dto.estoqueAtual() != null) {
            insumo.setEstoqueAtual(dto.estoqueAtual());
        }
        if (dto.estoqueMinimo() != null) {
            insumo.setEstoqueMinimo(dto.estoqueMinimo());
        }
        if (dto.valorUnitario() != null) {
            insumo.setValorUnitario(dto.valorUnitario());
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
    public void deletar(Long id) throws Exception {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new Exception("Material não encontrado"));

        insumo.setAtivo(false);
        insumoRepository.save(insumo);
    }
}
