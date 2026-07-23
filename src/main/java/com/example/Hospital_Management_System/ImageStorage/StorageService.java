package com.example.Hospital_Management_System.ImageStorage;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageService implements SupabaseStorageService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.bucket}")
    private String bucketName;

    @Value("${supabase.service.key}")
    private String serviceKey;

    public String uploadImage(MultipartFile file) throws IOException {

        String originalFileName = file.getOriginalFilename();

        String safeFileName = originalFileName.replaceAll("[^a-zA-Z0-9._-]", "_");


        String fileName = UUID.randomUUID() + "_" + safeFileName;

        // Folder inside bucket
        String objectPath = "Parent_identification/" + fileName;

        // Encode only the object path
        String encodedObjectPath = URLEncoder.encode(objectPath, StandardCharsets.UTF_8)
                .replace("+", "%20")
                .replace("%2F", "/");

        // Upload URL
        String uploadUrl = supabaseUrl
                + "/storage/v1/object/"
                + bucketName
                + "/"
                + encodedObjectPath;

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uploadUrl))
                .header("Authorization", "Bearer " + serviceKey)
                .header("apikey", serviceKey)
                .header("Content-Type", file.getContentType())
                .POST(HttpRequest.BodyPublishers.ofByteArray(file.getBytes()))
                .build();

        try {

            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 201) {

                String imageUrl = supabaseUrl
                        + "/storage/v1/object/public/"
                        + bucketName
                        + "/"
                        + encodedObjectPath;

                return imageUrl;
            }

            throw new RuntimeException(
                    "Upload Failed\nStatus : "
                            + response.statusCode()
                            + "\nResponse : "
                            + response.body());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Upload interrupted", e);
        }
    }

    @Override
    public void deleteImage(String imageUrl) throws IOException {

        if (imageUrl == null || imageUrl.isBlank()) {
            return;
        }


        String prefix = supabaseUrl + "/storage/v1/object/public/" + bucketName + "/";

        if (!imageUrl.startsWith(prefix)) {
            throw new RuntimeException("Invalid Supabase image URL: " + imageUrl);
        }

        String objectPath = imageUrl.substring(prefix.length());
        String deleteUrl = supabaseUrl
                + "/storage/v1/object/"
                + bucketName
                + "/"
                + objectPath;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(deleteUrl))
                .header("Authorization", "Bearer " + serviceKey)
                .header("apikey", serviceKey)
                .DELETE()
                .build();

        try {
            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Delete Status : " + response.statusCode());
            System.out.println("Delete Response : " + response.body());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}