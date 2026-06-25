package com.github.victormhb.bmadesivos.service;

import com.github.victormhb.bmadesivos.dto.insumo.EntradaInsumoDTO;
import com.github.victormhb.bmadesivos.dto.insumo.InsumoDTO;
import com.github.victormhb.bmadesivos.dto.insumo.InsumoUpdateDTO;
import com.github.victormhb.bmadesivos.entity.Insumo;
import com.github.victormhb.bmadesivos.entity.MovimentacaoEstoque;
import com.github.victormhb.bmadesivos.enums.TipoInsumo;
import com.github.victormhb.bmadesivos.enums.TipoMovimentacao;
import com.github.victormhb.bmadesivos.repository.InsumoRepository;
import com.github.victormhb.bmadesivos.repository.MovimentacaoEstoqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InsumoService {

    private final InsumoRepository insumoRepository;
    private final MovimentacaoEstoqueRepository movimentacaoRepository;

    @Autowired
    public InsumoService(InsumoRepository insumoRepository, MovimentacaoEstoqueRepository movimentacaoRepository) {
        this.insumoRepository = insumoRepository;
        this.movimentacaoRepository = movimentacaoRepository;
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

        if (!nomeTratado.matches("[\\p{L}0-9 \\-.,()/&+']+")) {
            throw new Exception("Nome contém caracteres inválidos.");
        }

        if (dto.tipoInsumo() == null) {
            throw new Exception("O tipo do insumo é obrigatório.");
        }

        if (dto.estoqueAtual() == null || dto.estoqueAtual() < 0) {
            throw new Exception("Estoque atual inválido.");
        }

        if ((dto.tipoInsumo() == TipoInsumo.TINTA || dto.tipoInsumo() == TipoInsumo.RESINA)
                && (dto.valorUnitario() == null || dto.valorUnitario() < 0)) {
            throw new Exception("O valor unitário é obrigatório para " + dto.tipoInsumo().name().toLowerCase() + ".");
        }

        Insumo insumo = new Insumo();
        insumo.setNome(nomeTratado);
        insumo.setDescricao(dto.descricao() != null ? dto.descricao().trim() : null);
        insumo.setTipoInsumo(dto.tipoInsumo());
        insumo.setEstoqueAtual(dto.estoqueAtual());
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

            if (dto.valorRolo() != null && m2PorRolo != null && m2PorRolo > 0) {
                insumo.setValorRolo(dto.valorRolo());
                insumo.setValorUnitario(dto.valorRolo() / m2PorRolo);
            } else if (dto.valorUnitario() != null) {
                insumo.setValorUnitario(dto.valorUnitario());
                if (m2PorRolo != null && m2PorRolo > 0) {
                    insumo.setValorRolo(dto.valorUnitario() * m2PorRolo);
                }
            }
        } else {
            // Tinta e Resina
            if (dto.valorUnitario() != null) {
                insumo.setValorUnitario(dto.valorUnitario());
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

        double estoqueAntes = insumo.getEstoqueAtual();

        if (dto.getNome() != null && !dto.getNome().trim().isEmpty()) {
            String nomeTratado = dto.getNome().trim();

            if (nomeTratado.length() < 3) {
                throw new Exception("Nome deve ter no mínimo 3 caracteres.");
            }

            if (!nomeTratado.matches("[\\p{L}0-9 \\-.,()/&+']+")) {
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

            boolean editouRolosOuDimensoes = dto.getQuantidadeRolos() != null
                    || dto.getLargura() != null
                    || dto.getComprimento() != null
                    || dto.getMetrosQuadrados() != null;

            if (editouRolosOuDimensoes && m2PorRolo != null && insumo.getQuantidadeRolos() != null) {
                insumo.setEstoqueAtual(m2PorRolo * insumo.getQuantidadeRolos());
            }

            if (dto.getValorRolo() != null && m2PorRolo != null && m2PorRolo > 0) {
                insumo.setValorRolo(dto.getValorRolo());
                insumo.setValorUnitario(dto.getValorRolo() / m2PorRolo);
            } else if (dto.getValorUnitario() != null) {
                insumo.setValorUnitario(dto.getValorUnitario());
                if (m2PorRolo != null && m2PorRolo > 0) {
                    insumo.setValorRolo(dto.getValorUnitario() * m2PorRolo);
                }
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

        Insumo salvo = insumoRepository.save(insumo);

        double diferenca = salvo.getEstoqueAtual() - estoqueAntes;
        if (Math.abs(diferenca) > 0.01) {
            MovimentacaoEstoque mov = new MovimentacaoEstoque();
            mov.setInsumo(salvo);
            mov.setQuantidade(Math.abs(diferenca));
            mov.setValorUnitario(salvo.getValorUnitario() != null ? salvo.getValorUnitario() : 0.0);
            mov.setTipo(TipoMovimentacao.AJUSTE);
            mov.setObservacao(dto.getObservacaoAjuste() != null
                    ? dto.getObservacaoAjuste().trim()
                    : (diferenca > 0 ? "Ajuste manual: acréscimo de estoque" : "Ajuste manual: redução de estoque"));
            movimentacaoRepository.save(mov);
        }

        return salvo;
    }

    @Transactional
    public void baixarEstoque(Long id, Double qtdConsumida) throws Exception {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new Exception("Material não encontrado"));

        if (insumo.getEstoqueAtual() < qtdConsumida)
            throw new Exception("Estoque insuficiente de " + insumo.getNome());

        insumo.setEstoqueAtual(insumo.getEstoqueAtual() - qtdConsumida);
        insumoRepository.save(insumo);

        MovimentacaoEstoque mov = new MovimentacaoEstoque();
        mov.setInsumo(insumo);
        mov.setQuantidade(qtdConsumida);
        mov.setTipo(TipoMovimentacao.SAIDA_INSUMO);
        mov.setValorUnitario(insumo.getValorUnitario() != null ? insumo.getValorUnitario() : 0.0);
        mov.setObservacao("Baixa automática por ordem de produção");
        movimentacaoRepository.save(mov);
    }

    @Transactional
    public void deletarInsumo(Long id) throws Exception {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new Exception("Material não encontrado"));

        insumo.setAtivo(false);
        insumoRepository.save(insumo);
    }

    @Transactional
    public Insumo registrarEntrada(Long id, EntradaInsumoDTO dto) throws Exception {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new Exception("Material com ID " + id + " não foi encontrado"));

        if (dto.quantidade() == null || dto.quantidade() <= 0) {
            throw new Exception("A quantidade da entrada deve ser maior que zero.");
        }

        double quantidadeMovimentacao;

        if (insumo.getTipoInsumo() == TipoInsumo.SUBSTRATO) {
            if (insumo.getMetrosQuadrados() == null || insumo.getMetrosQuadrados() <= 0) {
                throw new Exception("Insumo não possui m² por rolo cadastrado; informe largura e comprimento antes de registrar entrada.");
            }

            int rolosNovos = dto.quantidade().intValue();
            double m2PorRolo = insumo.getMetrosQuadrados();
            double m2Adicionados = m2PorRolo * rolosNovos;

            int rolosAtuais = insumo.getQuantidadeRolos() != null ? insumo.getQuantidadeRolos() : 0;
            insumo.setQuantidadeRolos(rolosAtuais + rolosNovos);
            insumo.setEstoqueAtual(insumo.getEstoqueAtual() + m2Adicionados);

            quantidadeMovimentacao = m2Adicionados;
        } else {
            insumo.setEstoqueAtual(insumo.getEstoqueAtual() + dto.quantidade());
            quantidadeMovimentacao = dto.quantidade();
        }

        if (dto.valorUnitario() != null && dto.valorUnitario() > 0) {
            insumo.setValorUnitario(dto.valorUnitario());
        }

        insumoRepository.save(insumo);

        MovimentacaoEstoque mov = new MovimentacaoEstoque();
        mov.setInsumo(insumo);
        mov.setQuantidade(quantidadeMovimentacao);
        mov.setValorUnitario(insumo.getValorUnitario() != null ? insumo.getValorUnitario() : 0.0);
        mov.setTipo(TipoMovimentacao.ENTRADA_INSUMO);
        mov.setObservacao("Entrada de estoque (compra)");
        movimentacaoRepository.save(mov);

        return insumo;
    }
}