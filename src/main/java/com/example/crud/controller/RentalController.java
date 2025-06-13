package com.example.crud.controller;

import com.example.crud.model.Rental;
import com.example.crud.model.Motorcycle;
import com.example.crud.model.User;
import com.example.crud.service.RentalService;
import com.example.crud.service.MotorcycleService;
import com.example.crud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/rentals")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @Autowired
    private MotorcycleService motorcycleService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String listRentals(Model model, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        List<Rental> userRentals = rentalService.findByUser(user);
        model.addAttribute("rentals", userRentals);
        return "user";
    }

    @GetMapping("/available")
    public String showAvailableMotorcycles(Model model) {
        List<Motorcycle> motorcycles = motorcycleService.findByStatus("Available");
        model.addAttribute("motorcycles", motorcycles);
        return "available-motorcycles";
    }

    @GetMapping("/create/{motorcycleId}")
    public String showRentalForm(@PathVariable Long motorcycleId, Model model) {
        Motorcycle motorcycle = motorcycleService.findById(motorcycleId);
        model.addAttribute("motorcycle", motorcycle);
        return "rental-form";
    }

    @PostMapping("/create")
    public String createRental(@RequestParam Long motorcycleId,
                              @RequestParam int duration,
                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                              Authentication authentication,
                              RedirectAttributes redirectAttributes) {
        User user = userService.findByUsername(authentication.getName());
        if (rentalService.findActiveRentalByUser(user) != null) {
            redirectAttributes.addFlashAttribute("error", "Maksimal hanya 1 rental motor per akun, tolong kembalikan motor terlebih dahulu.");
            return "redirect:/user";
        }
        Motorcycle motorcycle = motorcycleService.findById(motorcycleId);
        Rental rental = rentalService.createRental(user, motorcycle, startDate.atStartOfDay(), duration);
        return "redirect:/rentals/payment/" + rental.getId();
    }

    @GetMapping("/current")
    public String redirectToUserDashboard() {
        return "redirect:/user";
    }

    @GetMapping("/history")
    public String rentalHistory(Model model, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        List<Rental> history = rentalService.findHistoryByUser(user);
        model.addAttribute("history", history);
        return "rental-history";
    }

    @PostMapping("/return/{rentalId}")
    public String returnRental(@PathVariable Long rentalId, Authentication authentication) {
        rentalService.completeRental(rentalId);
        return "redirect:/rentals/current";
    }

    @GetMapping("/receipt/{rentalId}")
    public String viewReceipt(@PathVariable Long rentalId,
                            Authentication authentication,
                            Model model) {
        User user = userService.findByUsername(authentication.getName());
        Rental rental = rentalService.findById(rentalId);
        
        if (rental == null || !rental.getUser().getId().equals(user.getId())) {
            return "redirect:/rentals?error=invalid";
        }

        model.addAttribute("rental", rental);
        return "receipt";
    }

    @GetMapping("/payment/{rentalId}")
    public String paymentPage(@PathVariable Long rentalId, @RequestParam(value = "paid", required = false) String paid, Model model) {
        Rental rental = rentalService.findById(rentalId);
        model.addAttribute("rental", rental);
        model.addAttribute("paid", paid != null);
        return "payment";
    }

    @PostMapping("/payment/{rentalId}")
    public String processPayment(@PathVariable Long rentalId) {
        // (Opsional) update status rental jika perlu
        return "redirect:/rentals/payment/" + rentalId + "?paid=true";
    }

    @GetMapping("/admin")
    public String adminRentals(Model model) {
        List<Rental> activeRentals = rentalService.findActiveRentals();
        List<Rental> completedRentals = rentalService.findByStatus("COMPLETED");
        model.addAttribute("activeRentals", activeRentals);
        model.addAttribute("completedRentals", completedRentals);
        return "rentals";
    }
}
