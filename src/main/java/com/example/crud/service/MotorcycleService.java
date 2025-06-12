package com.example.crud.service;

import com.example.crud.model.Motorcycle;
import com.example.crud.repository.MotorcycleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MotorcycleService {
    
    @Autowired
    private MotorcycleRepository repository;

    public List<Motorcycle> findAll() {
        return repository.findAll();
    }

    public Motorcycle save(Motorcycle motorcycle) {
        return repository.save(motorcycle);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Motorcycle findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public long countAvailableMotorcycles() {
        try {
            // Debug log
            System.out.println("Counting available motorcycles...");
            long count = repository.count(); // or repository.countByStatus("Available")
            System.out.println("Found " + count + " motorcycles");
            return count;
        } catch (Exception e) {
            System.err.println("Error counting motorcycles: " + e.getMessage());
            return 0;
        }
    }
}
