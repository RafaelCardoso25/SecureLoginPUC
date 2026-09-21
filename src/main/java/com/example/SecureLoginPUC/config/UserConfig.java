package com.example.SecureLoginPUC.config;

import com.example.SecureLoginPUC.model.User;
import com.example.SecureLoginPUC.repository.UserJsonRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserConfig {

    @Bean
    public CommandLineRunner seedUsers(
            UserJsonRepository repository,
            PasswordEncoder passwordEncoder,
            @Value("${app.seed.user.username}") String userUsername,
            @Value("${app.seed.user.email}") String userEmail,
            @Value("${app.seed.user.password}") String userPassword,
            @Value("${app.seed.admin.username}") String adminUsername,
            @Value("${app.seed.admin.email}") String adminEmail,
            @Value("${app.seed.admin.password}") String adminPassword) {

        return args -> {
            if (!repository.findAll().isEmpty()) {
                return;
            }
            repository.save(new User(userUsername, userEmail, passwordEncoder.encode(userPassword), "USER"));
            repository.save(new User(adminUsername, adminEmail, passwordEncoder.encode(adminPassword), "ADMIN"));
        };
    }
}
