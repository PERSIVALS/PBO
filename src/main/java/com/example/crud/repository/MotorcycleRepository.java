package com.example.crud.repository;

import com.example.crud.model.Motorcycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MotorcycleRepository extends JpaRepository<Motorcycle, Long> {
	long countByStatus(String status);
	List<Motorcycle> findByStatus(String status);
}
