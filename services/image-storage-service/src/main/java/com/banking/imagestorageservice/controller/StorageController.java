package com.banking.imagestorageservice.controller;

import com.banking.imagestorageservice.dto.StorageRequest;
import com.banking.imagestorageservice.dto.StorageResponse;
import com.banking.imagestorageservice.service.StorageService;
import com.banking.imagestorageservice.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/storage")
public class StorageController {

    private final StorageService storageService;

    @Autowired
    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping(value = "/store",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> storeImage(
            @RequestPart("imageName") String imageName,
            @RequestPart("imageFile") MultipartFile imageFile) {

        if (imageFile == null || imageFile.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ApiResponse(false, "Image file must not be empty"));
        }

        StorageRequest request = new StorageRequest();
        request.setImageName(imageName);
        request.setImageFile(imageFile);

        ApiResponse response = storageService.storeImage(request);
        if (response == null || !response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        }
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StorageResponse> getImageDetails(@PathVariable Long id) {
        StorageResponse details = storageService.getImageDetails(id);
        if (details == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(null);
        }
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(details);
    }

}
