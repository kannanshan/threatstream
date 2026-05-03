package com.threatstream.config;

import com.threatstream.model.User;
import com.threatstream.model.enums.Role;
import com.threatstream.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) return;

        String encodedPassword = passwordEncoder.encode("password123");

        userRepository.save(User.builder()
                .id("user-viewer")
                .username("viewer")
                .password(encodedPassword)
                .displayName("Sam Viewer")
                .role(Role.VIEWER)
                .build());

        userRepository.save(User.builder()
                .id("user-analyst")
                .username("analyst")
                .password(encodedPassword)
                .displayName("Alex Analyst")
                .role(Role.ANALYST)
                .build());

        userRepository.save(User.builder()
                .id("user-admin")
                .username("admin")
                .password(encodedPassword)
                .displayName("Admin User")
                .role(Role.ADMIN)
                .build());

        log.info("Loaded 3 seed users (viewer, analyst, admin) with password: password123");
    }
}
