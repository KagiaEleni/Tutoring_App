package com.tutoringapp.model;

public class Enrollment {
	
	 private int enrollment_id;
	 private int student_id;
	 private int course_id;
	 private int hours_per_week;
	 float monthly_fee;
	 private String courseName;
	 
	// Default constructor
	 public Enrollment() {
			}

	// Constructor
	 public Enrollment(int enrollment_id, int student_id, int course_id, int hours_per_week, float monthly_fee) {
		super();
		this.enrollment_id = enrollment_id;
		this.student_id = student_id;
		this.course_id = course_id;
		this.hours_per_week = hours_per_week;
		this.monthly_fee = monthly_fee;
		}
	 
	 
	// Getters and Setters
	public int getEnrollment_id() {return enrollment_id;}
	public void setEnrollment_id(int enrollment_id) {this.enrollment_id = enrollment_id;}

	public int getStudent_id() {return student_id;}
	public void setStudent_id(int student_id) {this.student_id = student_id;}

	public int getCourse_id() {return course_id;}
	public void setCourse_id(int course_id) {this.course_id = course_id;}

	public int getHours_per_week() {return hours_per_week;}
	public void setHours_per_week(int hours_per_week) {this.hours_per_week = hours_per_week;}

	public float getMonthly_fee() {return monthly_fee;}
	public void setMonthly_fee(float monthly_fee) {this.monthly_fee = monthly_fee;}
	
	public String getCourseName() {return courseName;}
    public void setCourseName(String courseName) {this.courseName = courseName;}

}
