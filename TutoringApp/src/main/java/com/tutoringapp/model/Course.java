package com.tutoringapp.model;

public class Course {
    private int course_id;
    private String name;
    private String description;
    private float hourly_rate;

    // Default constructor
    public Course() {
    }

    // Constructor
    public Course(int course_id, String name, float hourly_rate, String description) {
        this.course_id = course_id;
        this.name = name;
        this.hourly_rate = hourly_rate;
        this.description = description; 
    }

    // Getters and Setters
    public int getCourseId() { return course_id; }
    public void setCourseId(int course_id) { this.course_id = course_id; }

    public String getName() { return name; }
    public void setName(String course_name) { this.name = course_name; }

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

    public float getHourlyRate() { return hourly_rate; }
    public void setHourlyRate(float hourly_rate) { this.hourly_rate = hourly_rate; }
}
