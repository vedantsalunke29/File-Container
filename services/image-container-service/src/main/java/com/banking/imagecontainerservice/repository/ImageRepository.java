package com.banking.imagecontainerservice.repository;

import com.banking.imagecontainerservice.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, String> {
}
