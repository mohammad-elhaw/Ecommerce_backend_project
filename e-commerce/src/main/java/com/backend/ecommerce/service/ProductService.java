package com.backend.ecommerce.service;

import com.backend.ecommerce.api.dto.ProductDTO;
import com.backend.ecommerce.api.dto.ProductResponse;
import com.backend.ecommerce.exception.APIException;
import com.backend.ecommerce.exception.ResourceNotFoundException;
import com.backend.ecommerce.model.Category;
import com.backend.ecommerce.model.Inventory;
import com.backend.ecommerce.model.Product;
import com.backend.ecommerce.model.repository.CategoryRepo;
import com.backend.ecommerce.model.repository.InventoryRepo;
import com.backend.ecommerce.model.repository.ProductRepo;
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


@Service
@Transactional
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final CategoryRepo categoryRepo;
    private final ProductRepo productRepo;
    private final InventoryRepo inventoryRepo;
    private final ModelMapper mapper;

    @SneakyThrows
    @Override
    public void createProduct(ProductDTO productDTO, Long categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category", "categoryId", categoryId));

        boolean isProductNotPresent = true;

        List<Product> products = category.getProducts();
        for(int i = 0; i < products.size(); ++i){
            if(products.get(i).getProductName().equals(productDTO.getProductName())
                    && products.get(i).getShortDescription().equals(productDTO.getShortDescription())){
                isProductNotPresent = false;
                break;
            }
        }
        if(isProductNotPresent){
            Product product = new Product();
            product.setProductName(productDTO.getProductName());
            product.setImage("default.png");
            product.setCategory(category);
            product.setDiscountPercent(productDTO.getDiscountPercent());
            double discountPrice = productDTO.getPrice() - ((productDTO.getDiscountPercent() * 0.01) * productDTO.getPrice());
            product.setDiscountPrice(discountPrice);
            product.setLongDescription(productDTO.getLongDescription());
            product.setShortDescription(productDTO.getShortDescription());
            product.setPrice(productDTO.getPrice());
            product = productRepo.save(product);

            Inventory inventory = new Inventory(productDTO.getQuantity(), product);
            inventoryRepo.save(inventory);
        }else{
            throw new APIException("Product already exists.");
        }
    }

    @Override
    public void deleteProduct(Long productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product", "productId", productId));
        productRepo.delete(product);
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")? Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber,pageSize, sortByAndOrder);
        Page<Product> pageProducts = productRepo.findAll(pageable);
        List<Product> products = pageProducts.getContent();
        return createProductResponse(products, pageProducts);
    }

    @SneakyThrows
    @Override
    public ProductResponse getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category", "categoryId", categoryId));

        Sort sortAndOrder = sortOrder.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortAndOrder);
        Page<Product> pageProducts = productRepo.findByCategory(category, pageable);
        List<Product> products = pageProducts.getContent();

        if(products.size() == 0){
            throw new APIException(category.getCategoryName() + " category does not contain any product.");
        }
        return createProductResponse(products, pageProducts);
    }

    @SneakyThrows
    @Override
    public ProductResponse getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageProducts = productRepo.findByProductNameStartingWithIgnoreCase(keyword, pageable);
        List<Product> products = pageProducts.getContent();

        if(products.size() == 0){
            throw new APIException("Products not found with the keyword: " + keyword);
        }
        return createProductResponse(products, pageProducts);
    }

    private ProductResponse createProductResponse(List<Product> products, Page<Product> pageProducts) {
        List<ProductDTO> productDTOS = products.stream()
                .map(product -> mapper.map(product, ProductDTO.class)).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());

        return productResponse;
    }
}
