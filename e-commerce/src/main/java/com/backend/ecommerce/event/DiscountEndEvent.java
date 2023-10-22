package com.backend.ecommerce.event;

import com.backend.ecommerce.model.Product;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Setter
@Getter
public class DiscountEndEvent extends ApplicationEvent {

    private Product product;
    public DiscountEndEvent(Product product) {
        super(product);
        this.product = product;
    }
}
