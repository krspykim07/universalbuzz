package com.vktech.universalbuzz.controller;

import com.vktech.universalbuzz.service.AppUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final AppUserService appUserService;

    public AuthController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping("/signup")
    public String signup(String fullName,
                         String username,
                         String email,
                         String password,
                         RedirectAttributes redirectAttributes) {

        try {
            appUserService.registerUser(fullName, username, email, password);
            redirectAttributes.addFlashAttribute("signupSuccess",
                "Account created successfully. You can now log in.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("signupError", ex.getMessage());
        }

        return "redirect:/#authSection";
    }
}
