package com.example.crud.repository;

import com.example.crud.model.Rental;
import com.example.crud.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
	List<Rental> findByUser(User user);
	long countByStatus(String status);
	Rental findByUserAndStatus(User user, String status);
	List<Rental> findByUserAndStatusNot(User user, String status);
	Page<Rental> findByUserAndStatusNot(User user, String status, Pageable pageable);
	@Query("SELECT COUNT(DISTINCT r.user.id) FROM Rental r")
	long countDistinctUsers();
	List<Rental> findByStatus(String status);
}
