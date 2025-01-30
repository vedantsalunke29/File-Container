package com.banking.imagestorageservice.service;

import com.banking.imagestorageservice.dto.StorageRequest;
import com.banking.imagestorageservice.dto.StorageResponse;
import com.banking.imagestorageservice.utils.ApiResponse;

public interface StorageService {
    ApiResponse storeImage(StorageRequest request);

    StorageResponse getImageDetails(Long id);
}
