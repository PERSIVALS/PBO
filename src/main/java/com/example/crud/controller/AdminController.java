package com.example.crud.controller;

import com.example.crud.service.CustomerService;
import com.example.crud.service.MotorcycleService;
import com.example.crud.service.RentalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    
    private final MotorcycleService motorcycleService;
    private final RentalService rentalService;
    private final CustomerService customerService;

    @Autowired
    public AdminController(MotorcycleService motorcycleService, 
                         RentalService rentalService, 
                         CustomerService customerService) {
        this.motorcycleService = motorcycleService;
        this.rentalService = rentalService;
        this.customerService = customerService;
    }

@GetMapping("/admin")
public String adminDashboard(Model model) {
    logger.info("Fetching dashboard data...");
    
    // Ambil data untuk dashboard
    long activeRentals = rentalService.countActiveRentals();
    long availableMotorcycles = motorcycleService.countAvailableMotorcycles();
    long totalCustomers = customerService.countTotalCustomers();
    
    // Log values for debugging
    logger.info("Dashboard Data - Active Rentals: {}, Available Motorcycles: {}, Total Customers: {}", 
               activeRentals, availableMotorcycles, totalCustomers);

    // Kirim data ke halaman admin.html
    model.addAttribute("activeRentals", activeRentals);
    model.addAttribute("availableMotorcycles", availableMotorcycles);
    model.addAttribute("totalCustomers", totalCustomers);
    
    logger.info("Returning admin view");
    return "admin";
}
}

