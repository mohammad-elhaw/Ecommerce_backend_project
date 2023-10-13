package com.backend.ecommerce.exception;

public class ResourceNotFoundException extends RuntimeException {

    private String resourceName;
    private String field;
    private Long fieldId;

    public ResourceNotFoundException(String resourceName, String field, Long fieldId) {
        super(String.format("%s not found with %s: %d", resourceName, field, fieldId));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldId = fieldId;
    }
}
