package com.github.inventorysaleshub.config;

import com.github.inventorysaleshub.model.Role;
import com.github.inventorysaleshub.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleDataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleDataLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        if (roleRepository.findAll().isEmpty()) {
            Role admin = new Role();
            admin.setName("ADMIN");
            roleRepository.save(admin);

            Role user = new Role();
            user.setName("USER");
            roleRepository.save(user);

            System.out.println("Default roles ADMIN and USER created.");
        }
    }
}
