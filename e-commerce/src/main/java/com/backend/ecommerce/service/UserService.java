package com.backend.ecommerce.service;

import com.backend.ecommerce.exception.EmailNotFoundException;
import com.backend.ecommerce.model.LocalUser;
import com.backend.ecommerce.model.repository.UserRepo;
import com.backend.ecommerce.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepo userRepo;

    @SneakyThrows
    @Override
    public LocalUser getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        LocalUser user = userRepo.findByEmailIgnoreCase(email)
                .orElseThrow(()->new EmailNotFoundException("Email not found"));
        return user;
    }
}
