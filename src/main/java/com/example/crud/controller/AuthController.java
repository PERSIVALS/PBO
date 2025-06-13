package com.example.crud.controller;

import com.example.crud.model.User;
import com.example.crud.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // Add root mappings for backward compatibility
    @GetMapping("/login")
    public String redirectToAuthLogin() {
        return "redirect:/auth/login";
    }

    @GetMapping("/register")
    public String redirectToAuthRegister() {
        return "redirect:/auth/register";
    }

    @GetMapping("/auth/login")
    public String loginForm(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        return "login";
    }

    @PostMapping("/auth/login")
    public String login(@RequestParam String username, 
                       @RequestParam String password,
                       HttpServletRequest request, 
                       Model model,
                       RedirectAttributes redirectAttributes) {
        try {
            username = username.trim();
            System.out.println("Username input: '" + username + "'");
            User user = userService.findByUsername(username);
            if (user != null) {
                System.out.println("User found: " + user.getUsername());
                System.out.println("Password input: '" + password + "'");
                System.out.println("Password DB: '" + user.getPassword() + "'");
                System.out.println("Password match: " + userService.getPasswordEncoder().matches(password, user.getPassword()));
                if (userService.getPasswordEncoder().matches(password, user.getPassword())) {
                    HttpSession session = request.getSession();
                    session.setAttribute("user", user);

                    if ("ADMIN".equals(user.getRole())) {
                        return "redirect:/admin";
                    } else {
                        return "redirect:/user";
                    }
                } else {
                    redirectAttributes.addFlashAttribute("error", "Invalid username or password");
                    return "redirect:/auth/login?error";
                }
            } else {
                System.out.println("User not found");
                redirectAttributes.addFlashAttribute("error", "Invalid username or password");
                return "redirect:/auth/login?error";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Login failed: " + e.getMessage());
            return "redirect:/auth/login?error";
        }
    }

    @GetMapping("/auth/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/auth/register")
    public String register(@ModelAttribute User user, 
                          Model model,
                          RedirectAttributes redirectAttributes) {
        try {
            if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
                model.addAttribute("error", "Username is required");
                return "register";
            }
            if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
                model.addAttribute("error", "Email is required");
                return "register";
            }
            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                model.addAttribute("error", "Password is required");
                return "register";
            }
            if (user.getNo_telp() == null || user.getNo_telp().trim().isEmpty()) {
                model.addAttribute("error", "Phone number is required");
                return "register";
            }

            if (userService.isUsernameExists(user.getUsername())) {
                model.addAttribute("error", "Username already exists");
                model.addAttribute("user", user);
                return "register";
            }

            if (userService.isEmailExists(user.getEmail())) {
                model.addAttribute("error", "Email already exists");
                model.addAttribute("user", user);
                return "register";
            }

            user.setRole("USER");
            String encodedPassword = userService.getPasswordEncoder().encode(user.getPassword());
            user.setPassword(encodedPassword);

            userService.register(user);
            redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
            return "redirect:/auth/login";

        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            model.addAttribute("user", user);
            return "register";
        }
    }

    @GetMapping("/auth/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/auth/login?logout=true";
    }
}
    