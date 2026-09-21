package com.example.SecureLoginPUC.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final String from;

    public EmailService(JavaMailSender mailSender, @Value("${spring.mail.username}") String from) {
        this.mailSender = mailSender;
        this.from = from;
    }

    public void sendRecoveryEmail(String to, String link) {
        if (from == null || from.isBlank()) {
            System.out.println("[SMTP não configurado] Link de recuperação para " + to + ": " + link);
            return;
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject("SecureLogin PUC - Recuperação de senha");
            message.setText("Para redefinir sua senha, acesse o link abaixo:\n\n"
                    + link + "\n\nSe você não solicitou a recuperação, ignore este email.");
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println("[Falha no envio do email] Link de recuperação para " + to + ": " + link);
        }
    }
}
