package com.github.victormhb.bmadesivos.service;

import com.github.victormhb.bmadesivos.event.FuncionarioCriadoEvent;
import com.github.victormhb.bmadesivos.event.RecuperacaoSenhaEvent;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    public EmailService(JavaMailSender mailSender,  TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
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

    public void enviarSenhaTemporaria(String destinatario, String nomeFuncionario, String senhaTemporaria) throws Exception {
        Context context = new Context();
        context.setVariable("nome", nomeFuncionario);
        context.setVariable("senhaTemp", senhaTemporaria);

        String html = templateEngine.process("email/senha-temporaria", context);

        enviarHtml(destinatario, "BM Adesivos — Seu acesso foi criado", html);
    }

    public void enviarLinkRecuperacao(String destinatario, String nomeFuncionario, String tokenBruto) throws Exception {
        String link = frontendUrl + "/redefinir-senha?token=" + tokenBruto;

        Context context = new Context();
        context.setVariable("nome", nomeFuncionario);
        context.setVariable("link", link);

        String html = templateEngine.process("email/recuperacao-senha", context);

        enviarHtml(destinatario, "BM Adesivos - Recuperação de senha", html);
    }

    private void enviarHtml(String destinatario, String assunto, String html) throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

        helper.setTo(destinatario);
        helper.setSubject(assunto);
        helper.setText(html, true);

        mailSender.send(mimeMessage);
    }
}