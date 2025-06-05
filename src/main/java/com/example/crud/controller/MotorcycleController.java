package com.example.crud.controller;

import com.example.crud.model.Motorcycle;
import com.example.crud.service.MotorcycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/motorcycles")
public class MotorcycleController {

    @Autowired
    private MotorcycleService motorcycleService;

    @GetMapping
    public String listMotorcycles(Model model) {
        model.addAttribute("motorcycles", motorcycleService.findAll());
        return "motorcycles";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("motorcycle", new Motorcycle());
        return "motorcycle_form";
    }

    @PostMapping("/save")
    public String saveMotorcycle(@ModelAttribute Motorcycle motorcycle) {
        motorcycleService.save(motorcycle);
        return "redirect:/motorcycles";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("motorcycle", motorcycleService.findById(id));
        return "motorcycle_form";
    }

    @GetMapping("/delete/{id}")
    public String deleteMotorcycle(@PathVariable Long id) {
        motorcycleService.delete(id);
        return "redirect:/motorcycles";
    }
}
