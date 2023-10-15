package com.backend.ecommerce.api.config;

import com.cloudinary.Cloudinary;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AppConfig {

    @Value("${cloudinary.cloud.name}")
    private String CLOUD_NAME;
    @Value("${cloudinary.api.key}")
    private String API_KEY;
    @Value("${cloudinary.api.secret}")
    private String API_SECRET;

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public Cloudinary cloudinary(){

        Map config = new HashMap();
        config.put("cloud_name", CLOUD_NAME);
        config.put("api_key", API_KEY);
        config.put("api_secret", API_SECRET);
        config.put("secure", true);

        return new Cloudinary(config);
    }
}
