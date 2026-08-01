package com.example.Hospital_Management_System.Token;



import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.example.Hospital_Management_System.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;

@Service
public class JwtService {

    private final Key key = Jwts.SIG.HS256.key().build();

    public JwtService() {
        String secret = Encoders.BASE64.encode(key.getEncoded());
        System.out.println("===================================");
        System.out.println("JWT SECRET KEY");
        System.out.println(secret);
        System.out.println("===================================");
    }
    public Claims decodeToken(String token) {

        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    // Generate JWT Token
    public String generateToken(User user) {

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("role", user.getRole().name())
                .claim("employeeCode", user.getEmployeeCode())
                .claim("id", user.getId())
                .claim("profileImageUrl", user.getProfileImageUrl())
                .claim("firstName", user.getFirstName())
                .claim("lastname", user.getLastName())
                .claim("phone", user.getPhone())
                .claim("qualification", user.getQualification())
                .claim("status", user.getStatus())
                .claim("specialization", user.getSpecialization())
                .claim("licenseNumber", user.getLicenseNumber())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 Hours
                .signWith(key)
                .compact();
    }

 
    public String extractEmail(String token) {

        Claims claims = Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    
    public boolean validateToken(String token, User user) {

        String email = extractEmail(token);

        return email.equals(user.getEmail());
    }

}
