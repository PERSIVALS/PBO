package com.example.crud.controller;

import com.example.crud.model.User;
import com.example.crud.model.Rental;
import com.example.crud.service.UserService;
import com.example.crud.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private UserService userService;
    @Autowired
    private RentalService rentalService;

    @GetMapping
    public String listCustomers(Model model) {
        List<User> users = userService.findAll();
        List<Map<String, Object>> customers = new ArrayList<>();
        for (User user : users) {
            if (!"USER".equalsIgnoreCase(user.getRole())) continue;
            Map<String, Object> customerMap = new HashMap<>();
            customerMap.put("id", user.getId());
            customerMap.put("username", user.getUsername());
            customerMap.put("email", user.getEmail());
            customerMap.put("no_telp", user.getNo_telp());
            customerMap.put("role", user.getRole());
            Rental activeRental = rentalService.findActiveRentalByUser(user);
            customerMap.put("activeRental", activeRental);
            customers.add(customerMap);
        }
        model.addAttribute("customers", customers);
        model.addAttribute("totalCustomers", rentalService.countDistinctCustomers());
        model.addAttribute("activeRentals", rentalService.countActiveRentals());
        return "customers";
    }
}
