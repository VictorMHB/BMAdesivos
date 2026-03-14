package com.github.victormhb.bmadesivos.service;

import com.github.victormhb.bmadesivos.dto.adesivo.FichaTecnicaDTO;
import com.github.victormhb.bmadesivos.entity.FichaTecnica;
import com.github.victormhb.bmadesivos.entity.Insumo;
import com.github.victormhb.bmadesivos.entity.Adesivo;
import com.github.victormhb.bmadesivos.enums.TipoInsumo;
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

    public List<FichaTecnica> buscarPorAdesivo(Long adesivoId) throws Exception {
        Adesivo adesivo = adesivoRepository.findById(adesivoId)
                .orElseThrow(() -> new Exception("Adesivo não encontrado."));
        return fichaTecnicaRepository.findByAdesivoAndAtivoTrue(adesivo);
    }

    @Transactional
    public FichaTecnica adicionarInsumoFicha(Long adesivoId, FichaTecnicaDTO dto) throws Exception {
        Adesivo adesivo = adesivoRepository.findById(adesivoId)
                .orElseThrow(() -> new Exception("Adesivo não encontrado."));

        Insumo insumo = insumoRepository.findById(dto.insumoId())
                .orElseThrow(() -> new Exception("Insumo não encontrado."));

        if (insumo.getTipoInsumo() != TipoInsumo.TINTA) {
            if (dto.quantidade() == null || dto.quantidade() <= 0) {
                throw new Exception("Quantidade é obrigatória para insumos que não são tinta.");
            }
        }

        FichaTecnica itemInsumo = new FichaTecnica();
        itemInsumo.setAdesivo(adesivo);
        itemInsumo.setInsumo(insumo);
        itemInsumo.setQuantidade(dto.quantidade());
        itemInsumo.setAtivo(true);

        return fichaTecnicaRepository.save(itemInsumo);
    }

    @Transactional
    public void deletarInsumoFicha(Long id) throws Exception {
        FichaTecnica itemInsumo = fichaTecnicaRepository.findById(id)
                .orElseThrow(() -> new Exception("Item da ficha técnica não encontrado."));

        itemInsumo.setAtivo(false);
        fichaTecnicaRepository.save(itemInsumo);
    }
}