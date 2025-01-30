package com.banking.imagestorageservice.repository;

import com.banking.imagestorageservice.model.StoredImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageRepository extends JpaRepository<StoredImage, Long> {
}
