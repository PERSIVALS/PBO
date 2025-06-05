// AuthController.java
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
    public String login(@RequestParam String username, @RequestParam String password, HttpServletRequest request, Model model) {
        User user = userService.findByUsername(username);
        if (user != null && userService.getPasswordEncoder().matches(password, user.getPassword())){
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            if (user.getRole().equals("ADMIN")) return "redirect:/admin";
            else return "redirect:/user";
        } else {
            model.addAttribute("error", "Username atau Password salah");
            return "login";
        }
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user) {
        userService.register(user);
        return "redirect:/login";
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
}
