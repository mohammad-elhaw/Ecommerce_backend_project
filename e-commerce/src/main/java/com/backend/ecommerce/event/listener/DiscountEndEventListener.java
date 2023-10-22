package com.backend.ecommerce.event.listener;

import com.backend.ecommerce.event.DiscountEndEvent;
import com.backend.ecommerce.model.Product;
import com.backend.ecommerce.model.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DiscountEndEventListener implements ApplicationListener<DiscountEndEvent> {

    private final ProductRepo productRepo;

    @Override
    @Transactional
    public void onApplicationEvent(DiscountEndEvent event) {
        Product product = event.getProduct();
        product.setDiscountPercent(0);
        product.setDiscountPrice(0);
        product.setDiscountStartDate(null);
        product.setDiscountEndDate(null);
        productRepo.save(product);
    }
}
