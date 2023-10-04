package com.backend.ecommerce.service.interfaces;

import com.backend.ecommerce.api.dto.RegisterRequest;
import com.backend.ecommerce.exception.UserAlreadyExistsException;
import com.backend.ecommerce.model.LocalUser;

public interface IAdminService {
    public LocalUser createAdmin(RegisterRequest registerRequest) throws UserAlreadyExistsException;
}
