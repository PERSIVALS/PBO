package com.example.crud.service;

import com.example.crud.model.Rental;
import com.example.crud.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RentalService {

    @Autowired
    private RentalRepository repository;

    public List<Rental> findAll() {
        return repository.findAll();
    }

    public Rental save(Rental rental) {
        return repository.save(rental);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Rental findById(Long id) {
        return repository.findById(id).orElse(null);
    }
    public long countActiveRentals() {
    return repository.countByStatus("Active");
}

}
