package com.tutoringapp.model;

import java.util.List;

public class Tutor {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private float salary;
    private List<String> courses;
    
    // Default constructor
    public Tutor() {

	}

	// Constructor
    public Tutor(int id, String firstName, String lastName, String email, String phone, float salary) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.salary = salary;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
	public float getSalary() {return salary;}
	public void setSalary(float salary) {this.salary = salary;}

	public List<String> getCourses() {return courses;}
	public void setCourses(List<String> courses) {this.courses = courses;}
    
    
}

