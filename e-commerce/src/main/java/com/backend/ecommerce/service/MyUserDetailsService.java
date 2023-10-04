package com.backend.ecommerce.service;

import com.backend.ecommerce.model.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepo.findByEmailIgnoreCase(email)
                .map(MyCustomUserDetails::new)
                .orElseThrow(()->new UsernameNotFoundException("User not found"));
    }
}
