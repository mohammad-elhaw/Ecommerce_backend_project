package com.backend.ecommerce.service;

import com.backend.ecommerce.api.dto.CategoryResponse;
import com.backend.ecommerce.exception.APIException;
import com.backend.ecommerce.exception.ResourceNotFoundException;
import com.backend.ecommerce.model.Category;
import com.backend.ecommerce.model.Product;
import com.backend.ecommerce.model.repository.CategoryRepo;
import com.backend.ecommerce.service.interfaces.ICategoryService;
import com.backend.ecommerce.service.interfaces.IProductService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private final CategoryRepo categoryRepo;
    private final IProductService productService;
    private final ModelMapper mapper;

    @SneakyThrows
    @Override
    public void createCategory(Category category) {
        Optional<Category> savedCategory = categoryRepo.findByCategoryNameIgnoreCase(category.getCategoryName());
        if(savedCategory.isPresent()){
            throw new APIException("Category with the name " + category.getCategoryName() + " already exists.");
        }
        categoryRepo.save(category);
    }

    @Override
    public void updateCategory(Category category, Long categoryId) {
        Category savedCategory = categoryRepo.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category", "categoryId", categoryId));
        category.setCategoryId(savedCategory.getCategoryId());
        categoryRepo.save(category);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category", "CategoryId", categoryId));
        List<Product> products = category.getProducts();
        products.forEach(product -> {
            productService.deleteProduct(product.getProductId());
        });

        categoryRepo.delete(category);
    }

    @SneakyThrows
    @Override
    public CategoryResponse getCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder =sortOrder.equalsIgnoreCase("asc")? Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Category> pageCategories = categoryRepo.findAll(pageable);
        List<Category> categories = pageCategories.getContent();

        if(categories.size() == 0)
            throw new APIException("No category is created till now.");

        List<Category> categoriesContent = categories.stream()
                .map(category -> mapper.map(category, Category.class)).toList();

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoriesContent);
        categoryResponse.setPageNumber(pageCategories.getNumber());
        categoryResponse.setPageSize(pageCategories.getSize());
        categoryResponse.setTotalElement(pageCategories.getTotalElements());
        categoryResponse.setTotalPages(pageCategories.getTotalPages());
        categoryResponse.setLastPage(pageCategories.isLast());

        return categoryResponse;
    }
}
