package com.tutoringapp.model;

import java.time.LocalDate;
import java.util.List;

public class Student {
    private int id;
    private String firstName;
    private String lastName;
    private String fatherName;
    private String email;
    private String phone;
    private LocalDate registrationDate;
    private List<Enrollment> enrollments;

    // Default constructor
    public Student() {
    }
    
    // Constructor
    public Student(int id, String firstName, String lastName, String fatherName, String email, String phone, LocalDate registrationDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fatherName = fatherName;
        this.email = email;
        this.phone = phone;
        this.registrationDate = registrationDate;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getFatherName() { return fatherName; }
    public void setFatherName(String fatherName) { this.fatherName = fatherName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public LocalDate getRegistrationDate() { return registrationDate; } 
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; } 
    
    public List<Enrollment> getEnrollments() {return enrollments;}
    public void setEnrollments(List<Enrollment> enrollments) {this.enrollments = enrollments;}
}
