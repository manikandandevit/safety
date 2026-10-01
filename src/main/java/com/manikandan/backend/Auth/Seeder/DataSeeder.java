package com.manikandan.backend.Auth.Seeder;

import com.manikandan.backend.Auth.Entity.UserEntity;
import com.manikandan.backend.Auth.Enums.Role.Role;
import com.manikandan.backend.Auth.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        
        if (userRepository.findByEmail("superadmin@gmail.com").isEmpty()) {
            UserEntity superAdmin = new UserEntity();
            superAdmin.setUsername("Super Admin");
            superAdmin.setEmail("superadmin@gmail.com");
            superAdmin.setPassword(passwordEncoder.encode("Admin@123"));
            superAdmin.setRole(Role.SUPER_ADMIN);
            
            userRepository.save(superAdmin);
        }

        if (userRepository.findByEmail("user1@gmail.com").isEmpty()) {
            UserEntity user = new UserEntity();
            user.setUsername("User 1");
            user.setEmail("user1@gmail.com");
            user.setPassword(passwordEncoder.encode("User@123"));
            user.setRole(Role.USER);
            
            userRepository.save(user);
        }
    }
}
