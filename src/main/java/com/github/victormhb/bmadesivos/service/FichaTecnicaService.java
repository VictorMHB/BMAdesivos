package com.github.victormhb.bmadesivos.service;

import com.github.victormhb.bmadesivos.dto.FichaTecnicaDTO;
import com.github.victormhb.bmadesivos.entity.FichaTecnica;
import com.github.victormhb.bmadesivos.entity.Material;
import com.github.victormhb.bmadesivos.entity.Produto;
import com.github.victormhb.bmadesivos.repository.FichaTecnicaRepository;
import com.github.victormhb.bmadesivos.repository.MaterialRepository;
import com.github.victormhb.bmadesivos.repository.ProdutoRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FichaTecnicaService {

    private final FichaTecnicaRepository fichaTecnicaRepository;
    private final ProdutoRepository produtoRepository;
    private final MaterialRepository materialRepository;

    @Autowired
    public FichaTecnicaService(
            FichaTecnicaRepository fichaTecnicaRepository,
            ProdutoRepository produtoRepository,
            MaterialRepository materialRepository)
    {
        this.fichaTecnicaRepository = fichaTecnicaRepository;
        this.produtoRepository = produtoRepository;
        this.materialRepository = materialRepository;
    }

    @Transactional
    public FichaTecnica adicionarItem(FichaTecnicaDTO dto) throws Exception {
        if (dto.qtdNecessaria() == null || dto.qtdNecessaria() <= 0) {
            throw new Exception("A quantidade necessária deve ser maior que zero.");
        }

        Produto produto = produtoRepository.findById(dto.produtoId())
                .orElseThrow(() -> new Exception("Produto não encontrado."));

        Material material = materialRepository.findById(dto.materialId())
                .orElseThrow(() -> new Exception("Material não encontrado."));

        FichaTecnica item = new FichaTecnica();
        item.setProduto(produto);
        item.setMaterial(material);
        item.setQtdNecessaria(dto.qtdNecessaria());
        item.setAtivo(true);

        return fichaTecnicaRepository.save(item);
    }

    public List<FichaTecnica> buscarReceitaProduto(Long idProduto) throws Exception {
        Produto produto = produtoRepository.findById(idProduto)
                .orElseThrow(() -> new Exception("Produto não encontrado."));

        return fichaTecnicaRepository.findByProduto(produto);
    }

    @Transactional
    public void removerItem(Long id) throws Exception {
        FichaTecnica item = fichaTecnicaRepository.findById(id)
                .orElseThrow(() -> new Exception("Item da ficha técnica não encontrado."));

        item.setAtivo(false);
        fichaTecnicaRepository.save(item);
    }
}
