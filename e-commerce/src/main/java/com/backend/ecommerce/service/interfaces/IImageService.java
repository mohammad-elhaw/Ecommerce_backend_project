package com.backend.ecommerce.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface IImageService {
    String upload(MultipartFile file);
}
