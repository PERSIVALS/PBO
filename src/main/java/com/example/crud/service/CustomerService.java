package com.example.crud.service;

import com.example.crud.model.Customer;
import com.example.crud.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository repository;

    // Menampilkan semua data customer
    public List<Customer> findAll() {
        return repository.findAll();
    }

    // Menyimpan data customer baru atau update
    public Customer save(Customer customer) {
        return repository.save(customer);
    }

    // Menghapus data customer berdasarkan ID
    public void delete(Long id) {
        repository.deleteById(id);
    }

    // Mencari customer berdasarkan ID
    public Customer findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    // Menerima customer (ubah status jadi "Accepted")
    public void accept(Long id) {
        Customer customer = repository.findById(id).orElse(null);
        if (customer != null) {
            customer.setStatus("Accepted");
            repository.save(customer);
        }
    }

    // Menolak customer (ubah status jadi "Rejected")
    public void reject(Long id) {
        Customer customer = repository.findById(id).orElse(null);
        if (customer != null) {
            customer.setStatus("Rejected");
            repository.save(customer);
        }
    }
}
