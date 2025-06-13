package com.example.crud.controller;

import com.example.crud.model.User;
import com.example.crud.model.Rental;
import com.example.crud.service.MotorcycleService;
import com.example.crud.service.RentalService;
import com.example.crud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private MotorcycleService motorcycleService;
    @Autowired
    private RentalService rentalService;

    @GetMapping("/user")
    public String userDashboard(Model model, Authentication authentication,
                               @RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "4") int size) {
        User user = userService.findByUsername(authentication.getName());
        model.addAttribute("motorcycles", motorcycleService.findByStatus("Available"));
        model.addAttribute("currentRental", rentalService.findActiveRentalByUser(user));
        Pageable pageable = PageRequest.of(page, size);
        Page<Rental> historyPage = rentalService.findHistoryByUser(user, pageable);
        model.addAttribute("historyPage", historyPage);
        return "user";
    }

    @GetMapping("/profile")
    public String userProfile(Model model, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute User formUser, Authentication authentication, RedirectAttributes redirectAttributes) {
        User user = userService.findByUsername(authentication.getName());
        user.setEmail(formUser.getEmail());
        user.setNo_telp(formUser.getNo_telp());
        // Username tidak diubah di sini, jika ingin bisa diedit tambahkan validasi unik
        userService.register(user); // gunakan save/update jika ada, atau register jika sudah handle update
        redirectAttributes.addFlashAttribute("success", "Profile updated successfully.");
        return "redirect:/profile";
    }
}
