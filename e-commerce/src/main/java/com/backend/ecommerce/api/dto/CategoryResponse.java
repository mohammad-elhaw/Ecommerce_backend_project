package com.backend.ecommerce.api.dto;

import com.backend.ecommerce.model.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {

    private List<Category> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElement;
    private Integer totalPages;
    private boolean lastPage;

}
