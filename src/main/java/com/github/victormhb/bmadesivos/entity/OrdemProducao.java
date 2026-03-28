package com.github.victormhb.bmadesivos.entity;

import com.github.victormhb.bmadesivos.enums.StatusOrdem;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ordens_producao")
public class OrdemProducao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "adesivo_id", nullable = false)
    private Adesivo adesivo;

    @ManyToOne
    @JoinColumn(name = "funcionario_id", nullable = false)
    private Funcionario funcionario;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(nullable = false)
    private Integer qtdPedida;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusOrdem status = StatusOrdem.PENDENTE;

    private LocalDateTime dataAbertura = LocalDateTime.now();
    private LocalDateTime dataConclusao;

    @Column(nullable = false)
    private boolean ativo = true;

    public OrdemProducao() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Adesivo getAdesivo() { return adesivo; }
    public void setAdesivo(Adesivo adesivo) { this.adesivo = adesivo; }

    public Funcionario getFuncionario() { return funcionario; }
    public void setFuncionario(Funcionario funcionario) { this.funcionario = funcionario; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Integer getQtdPedida() { return qtdPedida; }
    public void setQtdPedida(Integer qtdPedida) { this.qtdPedida = qtdPedida; }

    public StatusOrdem getStatus() { return status; }
    public void setStatus(StatusOrdem status) { this.status = status; }

    public LocalDateTime getDataAbertura() { return dataAbertura; }
    public void setDataAbertura(LocalDateTime dataAbertura) { this.dataAbertura = dataAbertura; }

    public LocalDateTime getDataConclusao() { return dataConclusao; }
    public void setDataConclusao(LocalDateTime dataConclusao) { this.dataConclusao = dataConclusao; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}