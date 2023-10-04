package com.backend.ecommerce.service;

import com.backend.ecommerce.api.dto.RegisterRequest;
import com.backend.ecommerce.exception.UserAlreadyExistsException;
import com.backend.ecommerce.model.LocalUser;
import com.backend.ecommerce.model.Role;
import com.backend.ecommerce.model.repository.RoleRepo;
import com.backend.ecommerce.model.repository.UserRepo;
import com.backend.ecommerce.service.interfaces.IAdminService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;


@Service
@AllArgsConstructor
public class AdminService implements IAdminService {

    private UserRepo userRepo;
    private RoleRepo roleRepo;
    private PasswordEncoder passwordEncoder;

    @Override
    public LocalUser createAdmin(RegisterRequest registerRequest) throws UserAlreadyExistsException {

        if(userRepo.findByEmailIgnoreCase(registerRequest.getEmail()) != null){
            throw new UserAlreadyExistsException("User Already Exists.");
        }

        Role adminRole = roleRepo.findByRoleName("ROLE_ADMIN");
        LocalUser user = new LocalUser();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRoles(Arrays.asList(adminRole));
        user.setCreatedAt(LocalDateTime.now());
        user.setPhoneNumber(registerRequest.getPhoneNumber());

        return userRepo.save(user);
    }
}
