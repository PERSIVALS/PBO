package com.example.crud.controller;

import com.example.crud.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public String listCustomers(Model model) {
        model.addAttribute("customers", customerService.findAll());
        return "customers";
    }

    @PostMapping("/accept/{id}")
    public String acceptCustomer(@PathVariable Long id) {
        customerService.accept(id);
        return "redirect:/customers";
    }

    @PostMapping("/reject/{id}")
    public String rejectCustomer(@PathVariable Long id) {
        customerService.reject(id);
        return "redirect:/customers";
    }
}
