package com.vktech.universalbuzz.service;

import com.vktech.universalbuzz.model.AppUser;
import com.vktech.universalbuzz.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(String fullName, String username, String email, String password) {
        if (appUserRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username is already taken.");
        }

        if (appUserRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email is already registered.");
        }

        AppUser user = new AppUser();
        user.setFullName(fullName);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("ROLE_USER");

        appUserRepository.save(user);
    }

    public void createDefaultAdminIfMissing() {
        if (appUserRepository.existsByUsername("admin")) {
            return;
        }

        AppUser admin = new AppUser();
        admin.setFullName("Administrator");
        admin.setUsername("admin");
        admin.setEmail("admin@universalbuzz.local");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole("ROLE_ADMIN");

        appUserRepository.save(admin);
    }
}
