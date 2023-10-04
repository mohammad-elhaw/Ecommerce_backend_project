package com.backend.ecommerce.model.repository;

import com.backend.ecommerce.model.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepo extends JpaRepository<Privilege, Long> {
    Privilege findByPrivilegeName(String name);
}
