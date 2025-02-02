package com.banking.apigateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @GetMapping("/fallback/auth")
    public ResponseEntity<String> authFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Authentication Service is unavailable. Please try again later.");
    }

    @GetMapping("/fallback/file")
    public ResponseEntity<String> fileFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("File Service is currently unavailable");
    }

    @GetMapping("/fallback/image")
    public ResponseEntity<String> imageFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Image Storage Service is overloaded");
    }
}