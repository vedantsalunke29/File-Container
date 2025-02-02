package com.banking.imagestorageservice.service.implementation;

import com.banking.imagestorageservice.dto.StorageRequest;
import com.banking.imagestorageservice.dto.StorageResponse;
import com.banking.imagestorageservice.model.StoredImage;
import com.banking.imagestorageservice.repository.StorageRepository;
import com.banking.imagestorageservice.service.StorageService;
import com.banking.imagestorageservice.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestPart;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class StorageServiceImplementation implements StorageService {

    private final StorageRepository storageRepository;

    @Value("${app.image-directory}")
    private String IMAGE_DIRECTORY;

    @Value("${app.image-base-url}")
    private String IMAGE_BASE_URL;

    @Autowired
    public StorageServiceImplementation(StorageRepository storageRepository) {
        this.storageRepository = storageRepository;
    }

    @Override
    public ApiResponse storeImage(@RequestPart("request") StorageRequest request) {
        try {
            // Create the uploads directory if it doesn't exist
            Path directoryPath = Paths.get(IMAGE_DIRECTORY);
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            MultipartFile imageFile = request.getImageFile();
            String originalFilename = StringUtils.cleanPath(imageFile.getOriginalFilename());

            // Generate unique filename
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

            // Create complete file path
            Path filePath = directoryPath.resolve(uniqueFilename);

            // Save the file
            Files.copy(imageFile.getInputStream(), filePath);

            // Save the image details to the database
            StoredImage image = new StoredImage();
            image.setImageName(request.getImageName());
            image.setOriginalFilename(originalFilename);
            image.setStoredFilename(uniqueFilename);
            image.setImageUrl(IMAGE_BASE_URL + "/" + uniqueFilename);
            image.setContentType(imageFile.getContentType());
            image.setSize(imageFile.getSize());
            storageRepository.save(image);

            ApiResponse response = new ApiResponse(true, "Image stored successfully");
            response.setId(image.getId());
            response.setDownloadUrl(image.getImageUrl());

            return response;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store image: " + e.getMessage(), e);
        }
    }

    @Override
    public StorageResponse getImageDetails(Long id) {
        StoredImage image = storageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        StorageResponse response = new StorageResponse();
        response.setId(image.getId());
        response.setName(image.getImageName());
        response.setDownloadUrl(image.getImageUrl());
        return response;
    }
}
