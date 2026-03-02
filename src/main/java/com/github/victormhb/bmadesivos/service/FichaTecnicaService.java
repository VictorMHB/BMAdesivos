package com.github.victormhb.bmadesivos.service;

import com.github.victormhb.bmadesivos.dto.FichaTecnicaDTO;
import com.github.victormhb.bmadesivos.entity.FichaTecnica;
import com.github.victormhb.bmadesivos.entity.Insumo;
import com.github.victormhb.bmadesivos.entity.Produto;
import com.github.victormhb.bmadesivos.repository.FichaTecnicaRepository;
import com.github.victormhb.bmadesivos.repository.InsumoRepository;
import com.github.victormhb.bmadesivos.repository.ProdutoRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FichaTecnicaService {

    private final FichaTecnicaRepository fichaTecnicaRepository;
    private final ProdutoRepository produtoRepository;
    private final InsumoRepository insumoRepository;

    @Autowired
    public FichaTecnicaService(
            FichaTecnicaRepository fichaTecnicaRepository,
            ProdutoRepository produtoRepository,
            InsumoRepository insumoRepository)
    {
        this.fichaTecnicaRepository = fichaTecnicaRepository;
        this.produtoRepository = produtoRepository;
        this.insumoRepository = insumoRepository;
    }

    @Transactional
    public FichaTecnica adicionarItem(FichaTecnicaDTO dto) throws Exception {
        if (dto.quantidade() == null || dto.quantidade() <= 0) {
            throw new Exception("A quantidade necessária deve ser maior que zero.");
        }

        if (dto.altura() == 0 || dto.altura() <= 0 || dto.comprimento() == 0 || dto.comprimento() <= 0) {
            throw new Exception("As dimensões devem ser maiores que zero");
        }

        Produto produto = produtoRepository.findById(dto.produtoId())
                .orElseThrow(() -> new Exception("Produto não encontrado."));

        Insumo insumo = insumoRepository.findById(dto.insumoId())
                .orElseThrow(() -> new Exception("Material não encontrado."));

        if (dto.valorUnitario() != null && dto.valorUnitario() > 0) {
            produto.setValorUnitario(dto.valorUnitario());
            produtoRepository.save(produto);
        }

        FichaTecnica item = new FichaTecnica();
        item.setProduto(produto);
        item.setInsumo(insumo);

        item.setQuantidade(dto.quantidade());
        item.setSubstrato(dto.substrato());
        item.setAltura(dto.altura());
        item.setComprimento(dto.comprimento());
        item.setTipoAdesivo(dto.tipoAdesivo());
        item.setQtdResina(dto.qtdResina());
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
