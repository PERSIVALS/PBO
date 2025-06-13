package com.example.crud.service;

import com.example.crud.model.Rental;
import com.example.crud.model.User;
import com.example.crud.model.Motorcycle;
import com.example.crud.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private MotorcycleService motorcycleService;

    public List<Rental> findByUser(User user) {
        return rentalRepository.findByUser(user);
    }

    public Rental findById(Long id) {
        return rentalRepository.findById(id).orElse(null);
    }

    @Transactional
    public Rental createRental(User user, Motorcycle motorcycle, LocalDateTime startDate, int duration) {
        // Check if motorcycle is available
        if (!"Available".equals(motorcycle.getStatus())) {
            throw new RuntimeException("Motorcycle is not available");
        }

        // Calculate end date and total price
        LocalDateTime endDate = startDate.plusDays(duration);
        double totalPrice = motorcycle.getPrice() * duration;

        // Create rental
        Rental rental = new Rental();
        rental.setUser(user);
        rental.setMotorcycle(motorcycle);
        rental.setStartDate(startDate);
        rental.setEndDate(endDate);
        rental.setTotalPrice(totalPrice);
        rental.setStatus("ACTIVE");

        // Update motorcycle status
        motorcycle.setStatus("Unavailable");
        motorcycleService.save(motorcycle);

        return rentalRepository.save(rental);
    }

    @Transactional
    public void completeRental(Long rentalId) {
        Rental rental = findById(rentalId);
        if (rental == null) {
            throw new RuntimeException("Rental not found");
        }

        if (!"ACTIVE".equals(rental.getStatus())) {
            throw new RuntimeException("Rental is not active");
        }

        // Update rental status
        rental.setStatus("COMPLETED");
        rentalRepository.save(rental);

        // Update motorcycle status
        Motorcycle motorcycle = rental.getMotorcycle();
        motorcycle.setStatus("Available");
        motorcycleService.save(motorcycle);
    }

    public List<Rental> findAll() {
        return rentalRepository.findAll();
    }

    public Rental save(Rental rental) {
        return rentalRepository.save(rental);
    }

    public void delete(Long id) {
        rentalRepository.deleteById(id);
    }

    public long countActiveRentals() {
        return rentalRepository.countByStatus("ACTIVE");
    }

    public Rental findActiveRentalByUser(User user) {
        return rentalRepository.findByUserAndStatus(user, "ACTIVE");
    }

    public List<Rental> findHistoryByUser(User user) {
        return rentalRepository.findByUserAndStatusNot(user, "ACTIVE");
    }

    public Page<Rental> findHistoryByUser(User user, Pageable pageable) {
        return rentalRepository.findByUserAndStatusNot(user, "ACTIVE", pageable);
    }

    public long countDistinctCustomers() {
        return rentalRepository.countDistinctUsers();
    }

    public List<Rental> findActiveRentals() {
        return rentalRepository.findByStatus("ACTIVE");
    }

    public List<Rental> findByStatus(String status) {
        return rentalRepository.findByStatus(status);
    }
}
