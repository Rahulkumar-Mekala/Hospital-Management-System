package com.example.Hospital_Management_System.GmailToken;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

@RestController
@RequestMapping("/gmail")
public class GmailTokenController {

    @Value("${google.oauth.client-id}")
    private String clientId;

    @Value("${google.oauth.client-secret}")
    private String clientSecret;
   
    private static final String REDIRECT_URI =
    		"https://hospital-management-system-8s44.onrender.com/gmail/callback";
    
    @GetMapping("/authorize")
    public String authorize() {

        String redirectUri = "https://hospital-management-system-8s44.onrender.com/gmail/callback";

        return "https://accounts.google.com/o/oauth2/v2/auth"
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=code"
                + "&scope=https://www.googleapis.com/auth/gmail.send"
                + "&access_type=offline"
                + "&prompt=consent";
    }

    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code) {

        System.out.println("Authorization Code: " + code);

        return "Authorization Code received. Copy it and call /gmail/token?code=" + code;
    }

  
    @PostMapping("/token")
    public String getToken(@RequestParam("code") String code) throws IOException {

        String redirectUri = "https://hospital-management-system-8s44.onrender.com/gmail/callback";

        GoogleTokenResponse tokenResponse =
                new GoogleAuthorizationCodeTokenRequest(
                        new NetHttpTransport(),
                        JacksonFactory.getDefaultInstance(),
                        clientId,
                        clientSecret,
                        code,
                        redirectUri)
                        .execute();

        return "Access Token:\n"
                + tokenResponse.getAccessToken()
                + "\n\nRefresh Token:\n"
                + tokenResponse.getRefreshToken();
    }
}