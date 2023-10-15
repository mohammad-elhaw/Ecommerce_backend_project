package com.backend.ecommerce.service;

import com.backend.ecommerce.exception.UploadImageFailureException;
import com.backend.ecommerce.service.interfaces.IImageService;
import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {

    private final Cloudinary cloudinary;

    @SneakyThrows
    @Override
    public String upload(MultipartFile file) {
        try{
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), Map.of());
            return (String) uploadResult.get("url");
        } catch (IOException e) {
            throw new UploadImageFailureException("fail to upload try again.");
        }
    }
}
