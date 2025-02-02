package com.banking.imagecontainerservice.service.implemention;

import com.banking.imagecontainerservice.dto.ImageResponse;
import com.banking.imagecontainerservice.model.Image;
import com.banking.imagecontainerservice.repository.ImageRepository;
import com.banking.imagecontainerservice.service.ImageService;
import com.banking.imagecontainerservice.utils.ApiResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ImageServiceImplementation implements ImageService {
    private static final Logger logger = LoggerFactory.getLogger(ImageServiceImplementation.class);

    private final ImageRepository imageRepository;
    private final RestTemplate restTemplate;

    @Value("${image-storage-service.url}")
    private String imageStorageServiceUrl;

    @Autowired
    public ImageServiceImplementation(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
        this.restTemplate = new RestTemplate();

        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(new FormHttpMessageConverter());
        converters.add(new MappingJackson2HttpMessageConverter());
        this.restTemplate.setMessageConverters(converters);
    }

    @Override
    @CircuitBreaker(name = "imageStorageCB", fallbackMethod = "uploadFallback")
    @Retry(name = "imageStorageRetry")
    public ResponseEntity<String> uploadImage(@RequestParam("imageName") String imageName,
                                              @RequestParam("imageFile") MultipartFile imageFile) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            HttpHeaders nameHeaders = new HttpHeaders();
            nameHeaders.setContentType(MediaType.TEXT_PLAIN);
            HttpEntity<String> nameEntity = new HttpEntity<>(imageName, nameHeaders);
            body.add("imageName", nameEntity);

            ByteArrayResource fileResource = new ByteArrayResource(imageFile.getBytes()) {
                @Override
                public String getFilename() {
                    return imageFile.getOriginalFilename();
                }
            };

            HttpHeaders fileHeaders = new HttpHeaders();
            fileHeaders.setContentType(MediaType.parseMediaType(
                    imageFile.getContentType() != null ?
                            imageFile.getContentType() :
                            MediaType.APPLICATION_OCTET_STREAM_VALUE
            ));

            HttpEntity<ByteArrayResource> fileEntity = new HttpEntity<>(fileResource, fileHeaders);
            body.add("imageFile", fileEntity);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            String storageEndpoint = imageStorageServiceUrl + "/api/storage/store";

            ResponseEntity<ApiResponse> response = restTemplate.exchange(
                    storageEndpoint,
                    HttpMethod.POST,
                    requestEntity,
                    ApiResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                ApiResponse responseBody = response.getBody();
                Image image = new Image();
                image.setName(imageName);
                image.setUrl(responseBody.getDownloadUrl());
                image.setType("image");
                imageRepository.save(image);

                return ResponseEntity.ok("Image stored and saved successfully and Image Id: " + responseBody.getId());
            } else {
                return ResponseEntity.status(response.getStatusCode())
                        .body("Failed to store image. Error: " + Objects.requireNonNull(response.getBody()).getMessage());
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing image file: " + e.getMessage());
        }
    }

    public ResponseEntity<String> uploadFallback(String imageName, MultipartFile imageFile, Throwable t) {
        logger.error("Image Storage Service is unavailable. Fallback triggered: {}", t.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Storage service is currently unavailable. Please try again later.");
    }

    @Override
    @CircuitBreaker(name = "imageStorageCB", fallbackMethod = "getImageFallback")
    @Retry(name = "imageStorageRetry")
    public ImageResponse getImage(Long id) {
        try {
            String getEndpoint = imageStorageServiceUrl + "/api/storage/{id}";

            ResponseEntity<ImageResponse> response = restTemplate.getForEntity(
                    getEndpoint,
                    ImageResponse.class,
                    id
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            } else {
                throw new RuntimeException("Failed to retrieve the image from storage.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving image: " + e.getMessage());
        }
    }

    public ImageResponse getImageFallback(Long id, Throwable t) {
        logger.error("Failed to retrieve image from storage. Returning fallback response.");
        return new ImageResponse(id, "Fallback image URL", "Fallback image description");
    }
}
