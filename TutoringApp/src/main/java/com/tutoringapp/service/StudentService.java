package com.tutoringapp.service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tutoringapp.config.DatabaseConnection;
import com.tutoringapp.model.Course;
import com.tutoringapp.model.Enrollment;
import com.tutoringapp.model.Student;

public class StudentService {

	public List<Student> getAllStudentsWithEnrollments() {
	    // Using a map to avoid duplicate students when multiple enrollments exist
	    Map<Integer, Student> studentMap = new HashMap<>();

	    String query = "SELECT s.student_id AS student_id, s.first_name, s.last_name, s.father_name AS father_name, s.email, s.phone_number, s.registration_date, "
	            + "e.enrollment_id, e.hours_per_week, e.monthly_fee, "
	            + "c.course_id, c.course_name, c.description, c.hourly_rate "
	            + "FROM students s "
	            + "LEFT JOIN enrollments e ON s.student_id = e.student_id "
	            + "LEFT JOIN courses c ON e.course_id = c.course_id";

	    try (Connection connection = DatabaseConnection.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(query);
	         ResultSet resultSet = preparedStatement.executeQuery()) {

	        while (resultSet.next()) {
	            // Fetch student details
	            int studentId = resultSet.getInt("student_id");
	            String firstName = resultSet.getString("first_name");
	            String lastName = resultSet.getString("last_name");
	            String fatherName = resultSet.getString("father_name");
	            String email = resultSet.getString("email");
	            String phoneNumber = resultSet.getString("phone_number");
	            LocalDate registrationDate = resultSet.getDate("registration_date").toLocalDate();

	            // Check if the student already exists in the map
	            Student student = studentMap.get(studentId);
	            if (student == null) {
	                // Create a new student if not already in the map
	                student = new Student(studentId, firstName, lastName, fatherName, email, phoneNumber, registrationDate);
	                student.setEnrollments(new ArrayList<>());  // Initialize the enrollment list
	                studentMap.put(studentId, student);
	            }

	            // Fetch enrollment details (if available)
	            int enrollmentId = resultSet.getInt("enrollment_id");
	            if (resultSet.wasNull()) { // No enrollment for this student
	                continue; // Skip adding enrollment details but keep the student
	            }

	            int hoursPerWeek = resultSet.getInt("hours_per_week");
	            float monthlyFee = resultSet.getFloat("monthly_fee");

	            // Fetch course details (if available)
	            int courseId = resultSet.getInt("course_id");
	            String courseName = resultSet.getString("course_name");
	            String description = resultSet.getString("description");
	            float hourlyRate = resultSet.getFloat("hourly_rate");

	            Course course = new Course(courseId, courseName, hourlyRate, description);

	            // Create and set enrollment
	            Enrollment enrollment = new Enrollment(enrollmentId, studentId, courseId, hoursPerWeek, monthlyFee);
	            enrollment.setCourseName(course.getName());

	            // Add the enrollment to the student's enrollment list
	            student.getEnrollments().add(enrollment);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    // Return the list of students, including those with no enrollments
	    return new ArrayList<>(studentMap.values());
	}



    public void addStudent(Student student) {
        String query = "INSERT INTO students (first_name, last_name, father_name, email, phone_number, registration_date) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, student.getFirstName());
            preparedStatement.setString(2, student.getLastName());
            preparedStatement.setString(3, student.getFatherName());
            preparedStatement.setString(4, student.getEmail());
            preparedStatement.setString(5, student.getPhone());
            preparedStatement.setDate(6, Date.valueOf(student.getRegistrationDate()));

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        student.setId(generatedKeys.getInt(1));
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Student getStudentById(int id) {
        String query = "SELECT * FROM students WHERE student_id = ?";
        Student student = null;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    String fatherName = resultSet.getString("father_name");
                    String email = resultSet.getString("email");
                    String phoneNumber = resultSet.getString("phone_number");
                    LocalDate registrationDate = resultSet.getDate("registration_date").toLocalDate();
                    student = new Student(id, firstName, lastName, fatherName, email, phoneNumber, registrationDate);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return student;
    }

    public void updateStudent(int id, Student student) {
        String query = "UPDATE Students SET first_name = ?, last_name = ?, father_name = ?, email = ?, phone_number = ?, registration_date = ? WHERE student_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, student.getFirstName());
            preparedStatement.setString(2, student.getLastName());
            preparedStatement.setString(3, student.getFatherName());
            preparedStatement.setString(4, student.getEmail());
            preparedStatement.setString(5, student.getPhone());

            // Assuming registrationDate is LocalDate
            LocalDate registrationDate = student.getRegistrationDate(); // Change this to LocalDate
            preparedStatement.setDate(6, Date.valueOf(registrationDate)); // Convert LocalDate to SQL Date

            preparedStatement.setInt(7, id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteStudent(int id) {
        String deleteEnrollmentsQuery = "DELETE FROM enrollments WHERE student_id = ?";
        String deleteStudentQuery = "DELETE FROM students WHERE student_id = ?";

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Start a transaction
            connection.setAutoCommit(false);

            try (PreparedStatement deleteEnrollmentsStmt = connection.prepareStatement(deleteEnrollmentsQuery);
                 PreparedStatement deleteStudentStmt = connection.prepareStatement(deleteStudentQuery)) {

                // Delete related enrollments first
                deleteEnrollmentsStmt.setInt(1, id);
                deleteEnrollmentsStmt.executeUpdate();

                // Then delete the student
                deleteStudentStmt.setInt(1, id);
                deleteStudentStmt.executeUpdate();

                // Commit transaction
                connection.commit();

            } catch (SQLException e) {
                connection.rollback(); // Roll back if an error occurs
                e.printStackTrace();
            } finally {
                connection.setAutoCommit(true); // Reset autocommit to default
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

