package com.github.victormhb.bmadesivos.service;

import com.github.victormhb.bmadesivos.dto.FichaTecnicaDTO;
import com.github.victormhb.bmadesivos.entity.FichaTecnica;
import com.github.victormhb.bmadesivos.entity.Insumo;
import com.github.victormhb.bmadesivos.entity.Adesivo;
import com.github.victormhb.bmadesivos.repository.FichaTecnicaRepository;
import com.github.victormhb.bmadesivos.repository.InsumoRepository;
import com.github.victormhb.bmadesivos.repository.AdesivoRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FichaTecnicaService {

    private final FichaTecnicaRepository fichaTecnicaRepository;
    private final AdesivoRepository adesivoRepository;
    private final InsumoRepository insumoRepository;

    @Autowired
    public FichaTecnicaService(
            FichaTecnicaRepository fichaTecnicaRepository,
            AdesivoRepository adesivoRepository,
            InsumoRepository insumoRepository)
    {
        this.fichaTecnicaRepository = fichaTecnicaRepository;
        this.adesivoRepository = adesivoRepository;
        this.insumoRepository = insumoRepository;
    }

    @Transactional
    public FichaTecnica adicionarFicha(FichaTecnicaDTO dto) throws Exception {
        if (dto.quantidade() == null || dto.quantidade() <= 0) {
            throw new Exception("A quantidade necessária deve ser maior que zero.");
        }

        Adesivo adesivo = adesivoRepository.findById(dto.adesivoId())
                .orElseThrow(() -> new Exception("Adesivo não encontrado."));

        Insumo insumo = insumoRepository.findById(dto.insumoId())
                .orElseThrow(() -> new Exception("Insumo não encontrado."));

        FichaTecnica item = new FichaTecnica();
        item.setAdesivo(adesivo);
        item.setInsumo(insumo);
        item.setQuantidade(dto.quantidade());
        item.setAtivo(true);

        return fichaTecnicaRepository.save(item);
    }

    public List<FichaTecnica> buscarReceitaAdesivo(Long adesivoId) throws Exception {
        Adesivo adesivo = adesivoRepository.findById(adesivoId)
                .orElseThrow(() -> new Exception("Adesivo não encontrado."));

        return fichaTecnicaRepository.findByAdesivo(adesivo);
    }

    @Transactional
    public void deletarFicha(Long id) throws Exception {
        FichaTecnica item = fichaTecnicaRepository.findById(id)
                .orElseThrow(() -> new Exception("Item da ficha técnica não encontrado."));

        item.setAtivo(false);
        fichaTecnicaRepository.save(item);
    }
}