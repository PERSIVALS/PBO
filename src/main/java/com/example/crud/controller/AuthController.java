// AuthController.java - FIXED
package com.example.crud.controller;

import com.example.crud.model.User;
import com.example.crud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, 
                       HttpServletRequest request, Model model) {
        try {
            User user = userService.findByUsername(username);
            if (user != null && userService.getPasswordEncoder().matches(password, user.getPassword())) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                
                if ("ADMIN".equals(user.getRole())) {
                    return "redirect:/admin";
                } else {
                    return "redirect:/user";
                }
            } else {
                model.addAttribute("error", "Username atau Password salah");
                return "login";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Login failed: " + e.getMessage());
            return "login";
        }
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User()); // Add empty user object
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) {
        try {
            // Debug: Print received user data
            System.out.println("Received user data: " + user.toString());
            
            // Validate input
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
            
            // Check if username already exists
            if (userService.isUsernameExists(user.getUsername())) {
                model.addAttribute("error", "Username already exists");
                model.addAttribute("user", user); // Keep form data
                return "register";
            }
            
            // Check if email already exists
            if (userService.isEmailExists(user.getEmail())) {
                model.addAttribute("error", "Email already exists");
                model.addAttribute("user", user); // Keep form data
                return "register";
            }
            
            // Set default role
            user.setRole("USER");
            
            // Encode password ONLY ONCE here
            String encodedPassword = userService.getPasswordEncoder().encode(user.getPassword());
            user.setPassword(encodedPassword);
            
            // Save user
            User savedUser = userService.register(user);
            System.out.println("User saved successfully: " + savedUser.toString());
            
            // Add success message and redirect to login
            model.addAttribute("success", "Registration successful! Please login.");
            return "redirect:/login?success=Registration successful";
            
        } catch (Exception e) {
            System.err.println("Registration error: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            model.addAttribute("user", user); // Keep form data
            return "register";
        }
    }

    @GetMapping("/user")
    public String userDashboard(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            if ("USER".equals(user.getRole())) {
                model.addAttribute("username", user.getUsername());
                return "user";
            }
        }
        return "redirect:/login";
    }

    @GetMapping("/admin")
    public String adminDashboard(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            if ("ADMIN".equals(user.getRole())) {
                model.addAttribute("username", user.getUsername());
                return "admin";
            }
        }
        return "redirect:/login";
    }
    
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login?logout=true";
    }
}