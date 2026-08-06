package com.github.victormhb.bmadesivos.entity;

import com.github.victormhb.bmadesivos.enums.StatusOrdem;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ordens_producao")
public class OrdemProducao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "ordem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemOrdem> itens = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "funcionario_id", nullable = false)
    private Funcionario funcionario;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusOrdem status = StatusOrdem.PENDENTE;

    @Column(nullable = false)
    private boolean arquivada = false;

    private LocalDateTime dataAbertura = LocalDateTime.now();
    private LocalDateTime dataPrazo;
    private LocalDateTime dataConclusao;

    @Column(nullable = false)
    private boolean ativo = true;

    public OrdemProducao() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public List<ItemOrdem> getItens() { return itens; }
    public void setItens(List<ItemOrdem> itens) { this.itens = itens; }
    public Funcionario getFuncionario() { return funcionario; }
    public void setFuncionario(Funcionario funcionario) { this.funcionario = funcionario; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public StatusOrdem getStatus() { return status; }
    public void setStatus(StatusOrdem status) { this.status = status; }
    public boolean isArquivada() { return arquivada; }
    public void setArquivada(boolean arquivada) { this.arquivada = arquivada; }
    public LocalDateTime getDataAbertura() { return dataAbertura; }
    public void setDataAbertura(LocalDateTime dataAbertura) { this.dataAbertura = dataAbertura; }

    public LocalDateTime getDataPrazo() {
        return dataPrazo;
    }

    public void setDataPrazo(LocalDateTime dataPrazo) {
        this.dataPrazo = dataPrazo;
    }

    public LocalDateTime getDataConclusao() { return dataConclusao; }
    public void setDataConclusao(LocalDateTime dataConclusao) { this.dataConclusao = dataConclusao; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}