package com.backend.ecommerce.service;

import com.backend.ecommerce.api.dto.CreateProductRequest;
import com.backend.ecommerce.model.Category;
import com.backend.ecommerce.model.Inventory;
import com.backend.ecommerce.model.Product;
import com.backend.ecommerce.model.repository.CategoryRepo;
import com.backend.ecommerce.model.repository.InventoryRepo;
import com.backend.ecommerce.model.repository.ProductRepo;
import com.backend.ecommerce.service.interfaces.IProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService implements IProductService {

    private CategoryRepo categoryRepo;
    private ProductRepo productRepo;
    private InventoryRepo inventoryRepo;

    @Override
    public void createProduct(CreateProductRequest request) {

        Optional<Category> firstLevel = categoryRepo.findByCategoryName(request.getFirstLevelCategory());

        if(firstLevel.isEmpty()){
            Category firstLevelCategory = new Category();
            firstLevelCategory.setCategoryName(request.getFirstLevelCategory());
            firstLevelCategory.setLevel(1);
            firstLevel = Optional.of(categoryRepo.save(firstLevelCategory));
        }

        Optional<Category> secondLevel = categoryRepo.findByCategoryName(request.getSecondLevelCategory());


        if(secondLevel.isEmpty()){
            Category secondLevelCategory = new Category();
            secondLevelCategory.setCategoryName(request.getSecondLevelCategory());
            secondLevelCategory.setParentCategory(firstLevel.get());
            secondLevelCategory.setLevel(2);
            secondLevel = Optional.of(categoryRepo.save(secondLevelCategory));
        }

        Product product = new Product();
        product.setName(request.getName());
        product.setShortDescription(request.getShortDescription());
        product.setLongDescription(request.getLongDescription());
        product.setDiscountPrice(request.getDiscountPrice());
        product.setDiscountPercent(request.getDiscountPercent());
        product.setPrice(request.getPrice());
        product.setSize(request.getSizes());
        product.setCategory(secondLevel.get());
        product.setImageUrl(request.getImageUrl());
        product.setCreatedAt(LocalDateTime.now());
        product = productRepo.save(product);

        Inventory inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setQuantity(request.getQuantity());
        inventoryRepo.save(inventory);
    }
}
