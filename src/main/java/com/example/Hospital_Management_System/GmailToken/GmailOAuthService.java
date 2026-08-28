package com.example.Hospital_Management_System.GmailToken;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class GmailOAuthService {

    @Value("${google.oauth.client-id}")
    private String clientId;

    @Value("${google.oauth.client-secret}")
    private String clientSecret;

    @Value("${google.oauth.access-token}")
    private String accessToken;

    @Value("${google.oauth.refresh-token}")
    private String refreshToken;

    public void sendEmail(String to, String subject, String body) {

        try {

            GoogleCredential credential = new GoogleCredential.Builder()
                    .setTransport(GoogleNetHttpTransport.newTrustedTransport())
                    .setJsonFactory(GsonFactory.getDefaultInstance())
                    .setClientSecrets(clientId, clientSecret)
                    .build()
                    .setAccessToken(accessToken)
                    .setRefreshToken(refreshToken);

            // Refresh access token if required
            credential.refreshToken();

            Gmail gmail = new Gmail.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(),
                    credential
            )
            .setApplicationName("Hospital Management System")
            .build();

            MimeMessage email = createEmail(
                    to,
                    subject,
                    body
            );

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            email.writeTo(buffer);

            String encodedEmail = Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(buffer.toByteArray());

            Message message = new Message();
            message.setRaw(encodedEmail);

            gmail.users()
                    .messages()
                    .send("me", message)
                    .execute();

            System.out.println("Email sent successfully to: " + to);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to send Gmail email",
                    e
            );
        }
    }

    private MimeMessage createEmail(
            String to,
            String subject,
            String body) throws Exception {

        Session session = Session.getDefaultInstance(
                System.getProperties(),
                null
        );

        MimeMessage email = new MimeMessage(session);

        email.setFrom(
                new InternetAddress("hospitalmanagementsystem.com@gmail.com")
        );

        email.addRecipient(
                jakarta.mail.Message.RecipientType.TO,
                new InternetAddress(to)
        );

        email.setSubject(subject, StandardCharsets.UTF_8.name());

        email.setContent(
        	    body,
        	    "text/html; charset=UTF-8"
        	);

        return email;
    }
}
