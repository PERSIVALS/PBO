package com.example.crud.controller;

import com.example.crud.model.Mahasiswa;
import com.example.crud.repository.MahasiswaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MahasiswaController {

    @Autowired
    private MahasiswaRepository repo;

    // Daftar mahasiswa
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("daftarMahasiswa", repo.findAll());
        return "index";
    }

    // Form tambah
    @GetMapping("/tambah")
    public String showFormTambah(Mahasiswa mahasiswa) {
        return "tambah";
    }

    // Proses tambah
    @PostMapping("/tambah")
    public String tambahMahasiswa(Mahasiswa mhs) {
        repo.save(mhs);
        return "redirect:/";
    }

    // Form edit
    @GetMapping("/edit/{nrp}")
    public String showFormEdit(@PathVariable String nrp, Model model) {
        Mahasiswa mhs = repo.findById(nrp).orElse(null);
        model.addAttribute("mahasiswa", mhs);
        return "edit";
    }

    // Proses update
    @PostMapping("/edit")
    public String updateMahasiswa(Mahasiswa mhs) {
        repo.save(mhs);
        return "redirect:/";
    }

    // Proses delete
    @PostMapping("/delete/{nrp}")
    public String deleteMahasiswa(@PathVariable String nrp) {
        repo.deleteById(nrp);
        return "redirect:/";
    }
}
