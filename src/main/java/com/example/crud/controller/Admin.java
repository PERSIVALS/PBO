package com.example.crud.controller;

import com.example.crud.service.MotorcycleService;
import com.example.crud.service.RentalService;
import com.example.crud.model.Rental;
import com.example.crud.model.Motorcycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class Admin {
    
    private static final Logger logger = LoggerFactory.getLogger(Admin.class);
    
    private final MotorcycleService motorcycleService;
    private final RentalService rentalService;
    @Autowired
    public Admin(MotorcycleService motorcycleService, 
                         RentalService rentalService) {
        this.motorcycleService = motorcycleService;
        this.rentalService = rentalService;
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard"; // pastikan ada dashboard.html di templates
    }

    @GetMapping("/admin")
    public String adminDashboard(Model model) {
        logger.info("Fetching dashboard data...");
        
        // Ambil data untuk dashboard
        long activeRentals = rentalService.countActiveRentals();
        long availableMotorcycles = motorcycleService.findByStatus("Available").size();
        long totalCustomers = rentalService.countDistinctCustomers();
        List<Rental> activeRentalList = rentalService.findActiveRentals();
        List<Motorcycle> availableMotorcycleList = motorcycleService.findByStatus("Available");
        
        // Log values for debugging
        logger.info("Dashboard Data - Active Rentals: {}, Available Motorcycles: {}, Total Customers: {}", 
                   activeRentals, availableMotorcycles, totalCustomers);

        // Kirim data ke halaman admin.html
        model.addAttribute("activeRentals", activeRentals);
        model.addAttribute("availableMotorcycles", availableMotorcycles);
        model.addAttribute("totalCustomers", totalCustomers);
        model.addAttribute("activeRentalList", activeRentalList);
        model.addAttribute("availableMotorcycleList", availableMotorcycleList);
        
        logger.info("Returning admin view");
        return "admin";
    }
}

