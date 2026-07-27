package com.github.victormhb.bmadesivos.service;

import com.github.victormhb.bmadesivos.entity.Funcionario;
import com.github.victormhb.bmadesivos.event.FuncionarioCriadoEvent;
import com.github.victormhb.bmadesivos.event.RecuperacaoSenhaEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    @EventListener
    public void aoCriarFuncionario(FuncionarioCriadoEvent event) {
        try {
            enviarSenhaTemporaria(event.getEmail(), event.getNome(), event.getSenhaTemp());
        } catch (Exception e) {
            System.out.println("[WARN] Funcionário criado, mas email não enviado: " + e.getMessage());
        }
    }

    @Async
    @EventListener
    public void aoRecuperacaoSenha(RecuperacaoSenhaEvent event) {
        try {
            enviarLinkRecuperacao(event.getEmail(), event.getNome(), event.getToken());
        } catch (Exception e) {
            System.out.println("[WARN] Token gerado, mas email não enviado: " + e.getMessage());
        }
    }

    public void enviarSenhaTemporaria(String destinatario, String nomeFuncionario, String senhaTemporaria) {
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setTo(destinatario);
        mensagem.setSubject("BM Adesivos — Seu acesso foi criado");
        mensagem.setText(
                "Olá, " + nomeFuncionario + "!\n\n" +
                        "Seu cadastro no sistema BM Adesivos foi realizado com sucesso.\n\n" +
                        "Sua senha temporária é: " + senhaTemporaria + "\n\n" +
                        "Por segurança, você será solicitado a trocar sua senha no primeiro acesso.\n\n" +
                        "Atenciosamente,\n" +
                        "Equipe BM Adesivos"
        );
        mailSender.send(mensagem);
    }

    public void enviarLinkRecuperacao(String destinatario, String nomeFuncionario, String tokenBruto) {
        String link = frontendUrl + "/redefinir-senha?token=" + tokenBruto;

        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setTo(destinatario);
        mensagem.setSubject("BM Adesivos - Recuperação de senha");
        mensagem.setText(
                "Olá, " + nomeFuncionario + "!\n\n" +
                        "Recebemos uma solicitação para redefinir sua senha.\n\n" +
                        "Clique no link abaixo para criar uma nova senha:\n" + link + "\n\n" +
                        "Este link expira em 30 minutos. Se você não solicitou isso, ignore este email.\n\n" +
                        "Atenciosamente,\n" +
                        "Equipe BM Adesivos"
        );
        mailSender.send(mensagem);
    }
}