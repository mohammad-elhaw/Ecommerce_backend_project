package com.backend.ecommerce.util;

import com.backend.ecommerce.model.Product;
import com.backend.ecommerce.model.repository.ProductRepo;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;

public class DiscountJob implements Runnable{

    private  Product product;
    private  ProductRepo productRepo;

    public DiscountJob(Product product, ProductRepo productRepo) {
        this.product = product;
        this.productRepo = productRepo;
    }

    @Override
    @Transactional
    public void run() {
        Instant currentInstant = Instant.now();
        Instant endInstant = product.getDiscountEndDate().atZone(ZoneId.systemDefault()).toInstant();
        if(currentInstant.isAfter(endInstant)){
            product.setDiscountPercent(0);
            product.setDiscountPrice(0);
            product.setDiscountStartDate(null);
            product.setDiscountEndDate(null);
            productRepo.save(product);
        }
    }
}
