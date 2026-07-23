package com.example.Hospital_Management_System.ImageStorage;

import java.io.IOException;

import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

public interface SupabaseStorageService {

    String uploadImage(MultipartFile file) throws IOException;
    void deleteImage(String imageUrl) throws IOException;

}