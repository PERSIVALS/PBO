package com.example.crud.controller;

import com.example.crud.model.Motorcycle;
import com.example.crud.service.MotorcycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.UUID;


@Controller
@RequestMapping("/motorcycles")
public class MotorcycleController {

    @Autowired
    private MotorcycleService motorcycleService;
    
    @Value("${file.upload.dir}")
    private String uploadDir;

    @GetMapping({"", "/"})
    public String showMotorcycleList(Model model) {
        model.addAttribute("motorcycle", new Motorcycle()); // untuk form tambah
        model.addAttribute("motorcycles", motorcycleService.findAll()); // untuk tabel
        return "motorcycles";
    }

    @PostMapping("/save")
    public String saveMotorcycle(@ModelAttribute Motorcycle motorcycle,
                               @RequestParam("imageFile") MultipartFile imageFile,
                               Model model) {
        try {
            // Buat absolute path untuk directory upload
            String uploadPath = new File("src/main/resources/" + uploadDir).getAbsolutePath();
            File uploadDirFile = new File(uploadPath);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }

            if (!imageFile.isEmpty()) {
                String filename = UUID.randomUUID() + "_" + StringUtils.cleanPath(imageFile.getOriginalFilename());
                File dest = new File(uploadPath + File.separator + filename);
                imageFile.transferTo(dest);
                motorcycle.setImagePath("/" + uploadDir + "/" + filename);
            }

            // Tambahkan logging untuk debug
            System.out.println("Saving motorcycle: " + motorcycle.getModel());
            motorcycleService.save(motorcycle);
            return "redirect:/motorcycles";
            
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            model.addAttribute("motorcycles", motorcycleService.findAll());
            return "motorcycles";
        }
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Motorcycle motorcycle = motorcycleService.findById(id);
        if (motorcycle == null) {
            return "redirect:/motorcycles";
        }
        model.addAttribute("motorcycle", motorcycle);
        model.addAttribute("motorcycles", motorcycleService.findAll());
        return "motorcycles";
    }

    @PostMapping("/delete/{id}")
    public String deleteMotorcycle(@PathVariable Long id) {
        try {
            Motorcycle motorcycle = motorcycleService.findById(id);
            if (motorcycle != null && motorcycle.getImagePath() != null) {
                File imageFile = new File("src/main/resources/static" + motorcycle.getImagePath());
                if (imageFile.exists()) {
                    imageFile.delete();
                }
            }
            motorcycleService.delete(id);
            return "redirect:/motorcycles";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/motorcycles?error=Delete failed";
        }
    }

    @GetMapping("/{id}")
    public String motorcycleDetail(@PathVariable Long id, Model model) {
        Motorcycle motorcycle = motorcycleService.findById(id);
        model.addAttribute("motorcycle", motorcycle);
        return "motorcycle-detail";
    }
}

