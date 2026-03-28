package com.vktech.universalbuzz.config;

import com.vktech.universalbuzz.service.AppUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedUsers(AppUserService appUserService) {
        return args -> appUserService.createDefaultAdminIfMissing();
    }
}
