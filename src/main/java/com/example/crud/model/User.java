// User.java (model) - FIXED
package com.example.crud.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String no_telp; // Changed to String to match HTML form

    @Column(nullable = false)
    private String role = "USER"; // Default role

    // Default constructor
    public User() {}

    // Constructor
    public User(String username, String password, String email, String no_telp) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.no_telp = no_telp;
        this.role = "USER";
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    // Fixed getter/setter names
    public String getNo_telp() { return no_telp; }
    public void setNo_telp(String no_telp) { this.no_telp = no_telp; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", no_telp='" + no_telp + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}