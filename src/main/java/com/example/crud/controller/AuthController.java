package com.example.crud.controller;

import com.example.crud.model.User;
import com.example.crud.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String loginForm() {
        return "login";
    }

    @PostMapping("/auth/login")
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

    @GetMapping("/auth/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/auth/register")
    public String register(@ModelAttribute User user, Model model) {
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
            return "redirect:/auth/login?success=Registration successful";

        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            model.addAttribute("user", user);
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
        return "redirect:/auth/login";
    }

    @GetMapping("/dashboard")
    public String adminRedirectInfo(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            if ("ADMIN".equals(user.getRole())) {
                return "redirect:/admin";
            }
        }
        return "redirect:/auth/login";
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
    