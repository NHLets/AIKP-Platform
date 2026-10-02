package org.afdb.aikp.modules.invitation.infrastructure.mail;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    public MailService(
            JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    public void sendInvitation(String to, String token) {

        String link =
                "https://portal.aikp.afdb.org/activate?token=" + token;

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(to);
        mail.setSubject("AIKP Platform Invitation");
        mail.setText(
                "Welcome to AIKP Platform\n\n"
                + "Activate your account:\n"
                + link
                + "\n\n"
                + "This link expires in 72 hours."
        );

        mailSender.send(mail);
    }
}
