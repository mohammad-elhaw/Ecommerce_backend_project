package com.backend.ecommerce.service;

import com.backend.ecommerce.model.LocalUser;
import com.backend.ecommerce.model.Privilege;
import com.backend.ecommerce.model.Role;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MyCustomUserDetails implements UserDetails {

    private LocalUser user;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getTheAuthorities(user.getRoles());
    }

    private Collection<? extends GrantedAuthority> getTheAuthorities(List<Role> roles) {
        return getGrantedAuthorities(getPrivilege(roles));
    }
    private List<String> getPrivilege(List<Role> roles) {
        List<String> privileges = new ArrayList<>(); //admin
        List<Privilege> collection = new ArrayList<>(); // all privilege

        for(Role role : roles){
            privileges.add(role.getRoleName()); // admin
            collection.addAll(role.getPrivileges()); //read write delete update
        }

        for(Privilege item : collection){
            privileges.add(item.getPrivilegeName());
        }
        return privileges;
    }

    private Collection<? extends GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for(String privilege : privileges){
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }
}
