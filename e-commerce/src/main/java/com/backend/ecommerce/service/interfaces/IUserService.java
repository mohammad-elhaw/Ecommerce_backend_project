package com.backend.ecommerce.service.interfaces;

import com.backend.ecommerce.model.LocalUser;

public interface IUserService {
    LocalUser getAuthenticatedUser();
}
