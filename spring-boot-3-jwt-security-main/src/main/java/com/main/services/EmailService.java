package com.main.services;

import com.main.entity.User;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    public void sendEmailToList(List<String> recipients, String subject, String message) {
        for (String to : recipients) {
            try {
                SimpleMailMessage email = new SimpleMailMessage();
                email.setTo(to);
                email.setSubject(subject);
                email.setText(message);
                email.setFrom("test@gmail.com");
                mailSender.send(email);
            } catch (MailException e) {
                logger.error("MailException failed to send email to: " + to, e);
            }
        }
    }
}

