package com.github.victormhb.bmadesivos.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "recuperacao_senha")
public class RecuperacaoSenha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "funcionario_id", nullable = false)
    private Funcionario funcionario;

    @Column(nullable = false, unique = true)
    private String tokenHash;

    @Column(nullable = false)
    private LocalDateTime dataExpiracao;

    @Column(nullable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Column(nullable = false)
    private boolean usado = false;

    public RecuperacaoSenha() {}

    public RecuperacaoSenha(Funcionario funcionario, String tokenHash, LocalDateTime dataExpiracao) {
        this.funcionario = funcionario;
        this.tokenHash = tokenHash;
        this.dataExpiracao = dataExpiracao;
    }

    public Long getId() {
        return id;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public String getTokenHash() {
        return tokenHash;
    }

    public LocalDateTime getDataExpiracao() {
        return dataExpiracao;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public boolean isUsado() {
        return usado;
    }

    public void setUsado(boolean usado) {
        this.usado = usado;
    }

    public boolean isValido() {
        return !usado && LocalDateTime.now().isBefore(dataExpiracao);
    }
}
