package com.banking.imagecontainerservice.controller;

import com.banking.imagecontainerservice.dto.ImageRequest;
import com.banking.imagecontainerservice.dto.ImageResponse;
import com.banking.imagecontainerservice.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(
            @RequestParam("imageName") String imageName,
            @RequestParam("imageFile") MultipartFile imageFile) {

        return ResponseEntity.ok(imageService.uploadImage(imageName, imageFile));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImageResponse> getImage(@PathVariable Long id) {
        return ResponseEntity.ok(imageService.getImage(id));
    }
}

