package com.example.crud.controller;

import com.example.crud.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        // Cek apakah user sudah login
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        // Tambahkan informasi user ke model
        model.addAttribute("user", user);
        model.addAttribute("role", user.getRole());
        
        // Arahkan ke template yang sesuai berdasarkan role
        if ("ADMIN".equals(user.getRole())) {
            return "admin"; // Template untuk admin
        } else {
            return "customer"; // Template untuk user biasa
        }
    }
}
