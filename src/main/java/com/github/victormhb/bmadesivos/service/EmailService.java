package com.github.victormhb.bmadesivos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
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
}