package com.backend.ecommerce.api.dto;

import com.backend.ecommerce.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginResponse {
    private Long id;
    private String email;
    private List<Role> role;
}
