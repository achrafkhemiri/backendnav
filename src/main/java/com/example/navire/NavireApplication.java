package com.example.navire;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import com.example.navire.repository.UserRepository;
import com.example.navire.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class NavireApplication {

    public static void main(String[] args) {
        SpringApplication.run(NavireApplication.class, args);
    }

    @Bean
    public CommandLineRunner seedAdminUser(UserRepository userRepository) {
        return args -> {
            String adminMail = "admin@gmail.com";
            String adminPassword = "css12345";
            if (!userRepository.existsByMail(adminMail)) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                User admin = new User();
                admin.setMail(adminMail);
                admin.setPassword(encoder.encode(adminPassword));
                userRepository.save(admin);
                System.out.println("Admin user created: " + adminMail);
            } else {
                System.out.println("Admin user already exists: " + adminMail);
            }
        };
    }

}
