// UserRepository.java
package com.example.crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.crud.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
