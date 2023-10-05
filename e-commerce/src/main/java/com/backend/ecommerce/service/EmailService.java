package com.backend.ecommerce.service;

import com.backend.ecommerce.exception.EmailTimeOutException;
import com.backend.ecommerce.model.LocalUser;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendEmail(String url, LocalUser user) throws MessagingException, UnsupportedEncodingException, EmailTimeOutException {
        String subject = "Email Verification";
        String senderName = "User Registration Portal Service";
        String mailContent = "<p> Hi, " + user.getFirstName()+ ", </p>" +
                "Please, follow the link below to complete your registration.</p>"+
                "<a> href=\"" + url + "\">Verify your email to activate your account</a>"+
                "<p> Thank you <br> Users Registration Portal Service";

        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("medo12345883@gmail.com", senderName);
        messageHelper.setTo(user.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        try{
            mailSender.send(message);
        }catch (MailException e){
            throw new EmailTimeOutException("Some thing wrong happen when sending email try again later.");
        }

    }
}
