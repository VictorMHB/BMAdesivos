package com.github.victormhb.bmadesivos.service;

import com.github.victormhb.bmadesivos.entity.Funcionario;
import com.github.victormhb.bmadesivos.entity.RecuperacaoSenha;
import com.github.victormhb.bmadesivos.event.RecuperacaoSenhaEvent;
import com.github.victormhb.bmadesivos.repository.FuncionarioRepository;
import com.github.victormhb.bmadesivos.repository.RecuperacaoSenhaRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class RecuperacaoSenhaService {

    private static final int EXPIRACAO_MINUTOS = 30;

    private final FuncionarioRepository funcionarioRepository;
    private final RecuperacaoSenhaRepository recuperacaoSenhaRepository;
    private final PasswordEncoder encoder;
    private final ApplicationEventPublisher publisher;
    private final SecureRandom secureRandom = new SecureRandom();

    public RecuperacaoSenhaService(FuncionarioRepository funcionarioRepository, RecuperacaoSenhaRepository recuperacaoSenhaRepository, PasswordEncoder encoder, ApplicationEventPublisher publisher) {
        this.funcionarioRepository = funcionarioRepository;
        this.recuperacaoSenhaRepository = recuperacaoSenhaRepository;
        this.encoder = encoder;
        this.publisher = publisher;
    }

    @Transactional
    public void solicitarRecuperacao(String email){
        funcionarioRepository.findByEmail(email).ifPresent(funcionario -> {
            String tokenBruto = gerarToken();
            String tokenHash = hashToken(tokenBruto);

            RecuperacaoSenha token = new  RecuperacaoSenha(funcionario, tokenHash, LocalDateTime.now().plusMinutes(EXPIRACAO_MINUTOS));
            recuperacaoSenhaRepository.save(token);

            publisher.publishEvent(
                    new RecuperacaoSenhaEvent(funcionario.getEmail(), funcionario.getNome(), tokenBruto)
            );
        });
    }

    public void redefinirSenha(String tokenBruto, String novaSenha) throws Exception{
        if (novaSenha == null && novaSenha.length() < 8){
            throw new Exception("A nova senha deve ter no minímo 8 caracteres.");
        }

        String tokenHash = hashToken(tokenBruto);

        RecuperacaoSenha token = recuperacaoSenhaRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new Exception("Link inválido ou expirado"));

        if (!token.isValido()) {
            throw new Exception("Link inválido ou expirado");
        }

        Funcionario funcionario = token.getFuncionario();
        funcionario.setSenha(encoder.encode(novaSenha));
        funcionario.setTrocarSenha(false);
        funcionarioRepository.save(funcionario);

        token.setUsado(true);
        recuperacaoSenhaRepository.save(token);
    }

    private String gerarToken() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar hash do token.", e);
        }
    }
}
