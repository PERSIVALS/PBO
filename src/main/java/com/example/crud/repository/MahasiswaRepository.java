package com.example.crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.crud.model.Mahasiswa;

public interface MahasiswaRepository extends JpaRepository<Mahasiswa, String> {
    
}
