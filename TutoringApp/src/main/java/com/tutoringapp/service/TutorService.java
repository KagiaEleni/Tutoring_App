package com.tutoringapp.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.tutoringapp.config.DatabaseConnection;
import com.tutoringapp.model.Tutor;

public class TutorService {

	public List<Tutor> getAllTutorsWithCourses() {
	    List<Tutor> tutors = new ArrayList<>();
	    String query = "SELECT t.tutor_id, t.first_name, t.last_name, t.email, t.phone_number, t.salary, "
	                 + "STRING_AGG(CAST(tc.course_id AS VARCHAR), ', ') AS course_ids "
	                 + "FROM Tutors t "
	                 + "LEFT JOIN tutorCourses tc ON t.tutor_id = tc.tutor_id "
	                 + "GROUP BY t.tutor_id";

	    try (Connection connection = DatabaseConnection.getConnection();
	         Statement statement = connection.createStatement();
	         ResultSet resultSet = statement.executeQuery(query)) {

	        while (resultSet.next()) {
	            int id = resultSet.getInt("tutor_id");
	            String firstName = resultSet.getString("first_name");
	            String lastName = resultSet.getString("last_name");
	            String email = resultSet.getString("email");
	            String phoneNumber = resultSet.getString("phone_number");
	            float salary = resultSet.getFloat("salary");
	            String courseIds = resultSet.getString("course_ids"); // Comma-separated course IDs

	            // Split course IDs into a list
	            List<String> courseIdList = courseIds != null ? Arrays.asList(courseIds.split(", ")) : new ArrayList<>();

	            Tutor tutor = new Tutor(id, firstName, lastName, email, phoneNumber, salary);
	            tutor.setCourses(courseIdList);  // Set the course IDs instead of names
	            tutors.add(tutor);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return tutors;
	}

	

    public void addTutor (Tutor tutor) {
        String query = "INSERT INTO Tutors (first_name, last_name, email, phone_number, salary) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, tutor.getFirstName());
            preparedStatement.setString(2, tutor.getLastName());
            preparedStatement.setString(3, tutor.getEmail());
            preparedStatement.setString(4, tutor.getPhone());
            preparedStatement.setFloat(5, tutor.getSalary());
           
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                    	tutor.setId(generatedKeys.getInt(1));
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Tutor getTutorById(int id) {
        String query = "SELECT * FROM Tutors WHERE tutor_id = ?";
        Tutor tutor = null;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    String email = resultSet.getString("email");
                    String phoneNumber = resultSet.getString("phone_number");
                    float salary = resultSet.getFloat("salary");
                    
                    tutor = new Tutor(id, firstName, lastName, email, phoneNumber, salary);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tutor;
    }

    public void updateTutor(int id, Tutor tutor) {
        String query = "UPDATE Tutors SET first_name = ?, last_name = ?, email = ?, phone_number = ?, salary = ? WHERE tutor_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, tutor.getFirstName());
            preparedStatement.setString(2, tutor.getLastName());
            preparedStatement.setString(3, tutor.getEmail());
            preparedStatement.setString(4, tutor.getPhone());
            preparedStatement.setFloat(5, tutor.getSalary());

            preparedStatement.setInt(6, id);
            
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTutor(int tutor_id) {
    	String deleteTutorsQuery = "DELETE FROM Tutors WHERE tutor_id = ?";
        String deleteTutorCoursesQuery = "DELETE FROM TutorCourses WHERE tutor_id = ?";
        
        Connection connection = null;

        try {
            connection = DatabaseConnection.getConnection();
            
            // Begin transaction
            connection.setAutoCommit(false);

            // Step 1: Delete tutorCourses associated with the course
            try (PreparedStatement deleteTutorCoursesStmt = connection.prepareStatement(deleteTutorCoursesQuery)) {
            	deleteTutorCoursesStmt.setInt(1, tutor_id);
            	deleteTutorCoursesStmt.executeUpdate();
            }

            // Step 2: Delete the course
            try (PreparedStatement deleteTutortmt = connection.prepareStatement(deleteTutorsQuery)) {
            	deleteTutortmt.setInt(1, tutor_id);
            	deleteTutortmt.executeUpdate();
            }

            // Commit transaction if both deletions were successful
            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                // Roll back transaction in case of error
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        } finally {
            // Restore auto-commit to true and close the connection if it was opened
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}

