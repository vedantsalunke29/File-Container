package com.banking.imagecontainerservice.service;
import com.banking.imagecontainerservice.dto.ImageRequest;
import com.banking.imagecontainerservice.utils.ApiResponse;
import com.banking.imagecontainerservice.dto.ImageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    ResponseEntity<String> uploadImage(String imageName, MultipartFile imageFile );
    ImageResponse getImage(Long id);
}

