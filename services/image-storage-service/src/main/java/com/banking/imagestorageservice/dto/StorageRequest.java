package com.banking.imagestorageservice.dto;

import org.springframework.web.multipart.MultipartFile;

public class StorageRequest {
    private String imageName;
    private MultipartFile imageFile;

    // Getters and Setters
    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }
}
