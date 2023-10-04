package com.backend.ecommerce.api.config;

import com.backend.ecommerce.model.LocalUser;
import com.backend.ecommerce.model.Privilege;
import com.backend.ecommerce.model.Role;
import com.backend.ecommerce.model.repository.PrivilegeRepo;
import com.backend.ecommerce.model.repository.RoleRepo;
import com.backend.ecommerce.model.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private PrivilegeRepo privilegeRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
        Privilege deletePrivilege = createPrivilegeIfNotFound("DELETE_PRIVILEGE");
        Privilege updatePrivilege = createPrivilegeIfNotFound("UPDATE_PRIVILEGE");


        List<Privilege> adminPrivilege = Arrays.asList(readPrivilege, writePrivilege,
                deletePrivilege, updatePrivilege);

        createRoleIfNotFound("ROLE_ADMIN", adminPrivilege);
        createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege));

        if(userRepo.findByEmailIgnoreCase("admin@gmail.com").isEmpty()){
            Role adminRole = roleRepo.findByRoleName("ROLE_ADMIN");
            LocalUser localUser = new LocalUser();
            localUser.setFirstName("admin");
            localUser.setLastName("admin");
            localUser.setEmail("admin@gmail.com");
            localUser.setPassword(passwordEncoder.encode("admin"));
            localUser.setRoles(Arrays.asList(adminRole));
            localUser.setEnabled(true);
            userRepo.save(localUser);
        }else return;
    }


    @Transactional
    private Privilege createPrivilegeIfNotFound(String name) {
        Privilege privilege = privilegeRepo.findByPrivilegeName(name);
        if(privilege == null){
            privilege = new Privilege(name);
            privilegeRepo.save(privilege);
        }
        return privilege;
    }

    @Transactional
    private void createRoleIfNotFound(String name, List<Privilege> privileges) {
        Role role = roleRepo.findByRoleName(name);
        if(role == null){
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepo.save(role);
        }
    }
}
