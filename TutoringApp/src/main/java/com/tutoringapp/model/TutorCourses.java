package com.tutoringapp.model;

public class TutorCourses {
	
	private int tutor_course_id;
	private int tutor_id;
	private int course_id;
	
	public TutorCourses() {
		super();
	}



	public TutorCourses(int tutor_course_id, int tutor_id, int course_id) {
		super();
		this.tutor_course_id = tutor_course_id;
		this.tutor_id = tutor_id;
		this.course_id = course_id;
	}

	public int getTutor_course_id() {return tutor_course_id;}
	public void setTutor_course_id(int tutor_course_id) {this.tutor_course_id = tutor_course_id;}

	public int getTutor_id() {return tutor_id;}
	public void setTutor_id(int tutor_id) {this.tutor_id = tutor_id;}

	public int getCourse_id() {return course_id;}
	public void setCourse_id(int course_id) {this.course_id = course_id;}
}
