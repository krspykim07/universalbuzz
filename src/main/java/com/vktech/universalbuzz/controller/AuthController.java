package com.vktech.universalbuzz.controller;

import com.vktech.universalbuzz.model.User;
import com.vktech.universalbuzz.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

   @PostMapping("/signup")
    public String signup(String fullName,
                        String username,
                        String email,
                        String password,
                        String confirmPassword) {

        if (userRepository.existsByUsername(username)) {
            return "redirect:/?signupError=usernameExists";
        }

        if (userRepository.existsByEmail(email)) {
            return "redirect:/?signupError=emailExists";
        }

        if (password.length() < 8) {
            return "redirect:/?signupError=passwordTooShort";
        }

        if (!password.equals(confirmPassword)) {
            return "redirect:/?signupError=passwordMismatch";
        }

        User user = new User();
        user.setFullName(fullName);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("USER");

        userRepository.save(user);

        return "redirect:/?signupSuccess=true";
    }
}