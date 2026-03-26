package com.vktech.universalbuzz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @PostMapping("/signup")
    public String signup(String fullName,
                         String username,
                         String email,
                         String password,
                         RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("signupSuccess",
                "Signup UI works! Next step is saving users to the database.");

        return "redirect:/#authSection";
    }
}