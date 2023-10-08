package com.backend.ecommerce.api.controller;

import com.backend.ecommerce.api.dto.CreateProductRequest;
import com.backend.ecommerce.service.interfaces.IProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/products")
@AllArgsConstructor
public class AdminProductController {

    private IProductService productService;

    @PostMapping(value = "/create")
    public ResponseEntity<?> createProduct(@Valid @RequestBody CreateProductRequest request){
        productService.createProduct(request);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
