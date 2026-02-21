package com.github.victormhb.bmadesivos.service;

import br.com.caelum.stella.validation.InvalidStateException;
import com.github.victormhb.bmadesivos.dto.MaterialDTO;
import com.github.victormhb.bmadesivos.entity.Material;
import com.github.victormhb.bmadesivos.repository.MaterialRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaterialService {

    private final MaterialRepository materialRepository;

    @Autowired
    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    public List<Material> listarTodos() {
        return materialRepository.findAll(Sort.by(Sort.Direction.ASC, "nome"));
    }

    public Material buscarPorId(Long id) throws Exception {
        return materialRepository.findById(id)
                .orElseThrow(() -> new Exception("Material com ID: " + id + " não foi encontrado"));
    }

    @Transactional
    public Material salvar(MaterialDTO dto) throws Exception {
        if (dto.nome() == null || dto.nome().trim().isEmpty()) {
            throw new Exception("O nome do material é obrigatório.");
        }

        Material material = new Material();
        material.setNome(dto.nome());
        material.setUnidadeMedida(dto.unidadeMedida());
        material.setEstoqueAtual(dto.estoqueAtual() != null ? dto.estoqueAtual() : 0.0);
        material.setEstoqueMinimo(dto.estoqueMinimo() != null ? dto.estoqueMinimo() : 0.0);
        material.setValorUnitario(dto.valorUnitario() != null ? dto.valorUnitario() : 0.0);
        material.setAtivo(true);

        return materialRepository.save(material);
    }

    @Transactional
    public Material atualizar(Long id, MaterialDTO dto) throws Exception {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new Exception("Material com ID " + id + " não foi encontrado"));

        if (dto.nome() != null && !dto.nome().trim().isEmpty()) {
            material.setNome(dto.nome());
        }
        if (dto.unidadeMedida() != null) {
            material.setUnidadeMedida(dto.unidadeMedida());
        }
        if (dto.estoqueAtual() != null) {
            material.setEstoqueAtual(dto.estoqueAtual());
        }
        if (dto.estoqueMinimo() != null) {
            material.setEstoqueMinimo(dto.estoqueMinimo());
        }
        if (dto.valorUnitario() != null) {
            material.setValorUnitario(dto.valorUnitario());
        }

        return materialRepository.save(material);
    }

    @Transactional
    public void baixarEstoque(Long id, Double qtdConsumida) throws Exception {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new Exception("Material não encontrado"));

        if (material.getEstoqueAtual() < qtdConsumida) {
            throw new Exception("Estoque insuficiente de " + material.getNome());
        }

        material.setEstoqueAtual(material.getEstoqueAtual() - qtdConsumida);
        materialRepository.save(material);

        if (material.getEstoqueAtual() <= material.getEstoqueMinimo()) {
            System.out.println("ALERTA: " + material.getNome() + " atingiu o nível crítico.");
        }
    }

    @Transactional
    public void deletar(Long id) throws Exception {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new Exception("Material não encontrado"));

        material.setAtivo(false);
        materialRepository.save(material);
    }
}
