package com.backend.ecommerce.service;

import com.backend.ecommerce.api.dto.CreateProductDTO;
import com.backend.ecommerce.api.dto.ProductResponse;
import com.backend.ecommerce.api.dto.ProductResponseDTO;
import com.backend.ecommerce.exception.APIException;
import com.backend.ecommerce.exception.ResourceNotFoundException;
import com.backend.ecommerce.model.Category;
import com.backend.ecommerce.model.Inventory;
import com.backend.ecommerce.model.Product;
import com.backend.ecommerce.model.repository.CategoryRepo;
import com.backend.ecommerce.model.repository.InventoryRepo;
import com.backend.ecommerce.model.repository.ProductRepo;
import com.backend.ecommerce.service.interfaces.IDiscountService;
import com.backend.ecommerce.service.interfaces.IImageService;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final CategoryRepo categoryRepo;
    private final ProductRepo productRepo;
    private final InventoryRepo inventoryRepo;
    private final ModelMapper mapper;
    private final IImageService imageService;
    private final IDiscountService discountService;

    @SneakyThrows
    @Override
    @Transactional
    public void createProduct(CreateProductDTO createProductDTO, Long categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category", "categoryId", categoryId));

        boolean isProductNotPresent = true;

        List<Product> products = category.getProducts();
        for (Product value : products) {
            if (value.getProductName().equals(createProductDTO.getProductName())
                    || value.getShortDescription().equals(createProductDTO.getShortDescription())) {
                isProductNotPresent = false;
                break;
            }
        }
        if(isProductNotPresent){
            Product product = new Product();
            product.setProductName(createProductDTO.getProductName());
            product.setCategory(category);
            product.setLongDescription(createProductDTO.getLongDescription());
            product.setShortDescription(createProductDTO.getShortDescription());
            product.setPrice(createProductDTO.getPrice());
            product = productRepo.save(product);
            discountService.addDiscountForProduct(product, createProductDTO);

            Inventory inventory = new Inventory(createProductDTO.getQuantity(), product);
            inventoryRepo.save(inventory);


        }else{
            throw new APIException("Product already exists.");
        }
    }


    @Override
    @Transactional
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

    @Override
    @Transactional
    public void updateProductImage(Long productId, MultipartFile file) {
        Product productFromDB = productRepo.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product", "productId", productId));

        String imageUrl = imageService.upload(file);
        productFromDB.setImageUrl(imageUrl);
        productRepo.save(productFromDB);
    }

    private ProductResponse createProductResponse(List<Product> products, Page<Product> pageProducts) {
        List<ProductResponseDTO> productResponseDTOS = products.stream()
                .map(product -> mapper.map(product, ProductResponseDTO.class)).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productResponseDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());

        return productResponse;
    }
}
