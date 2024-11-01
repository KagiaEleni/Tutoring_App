package com.tutoringapp.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.tutoringapp.config.DatabaseConnection;
import com.tutoringapp.model.Course;

public class CourseService {
    
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();

        String query = "SELECT course_id, course_name, hourly_rate, description FROM Courses";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int courseId = resultSet.getInt("course_id");
                String courseName = resultSet.getString("course_name");
                float hourlyRate = resultSet.getFloat("hourly_rate");
                String description = resultSet.getString("description");

                Course course = new Course(courseId, courseName, hourlyRate, description);
                courses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return courses;
    }
    
    public void addCourse(Course course) {
        String query = "INSERT INTO Courses (course_name, hourly_rate, description) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, course.getName());
            preparedStatement.setFloat(2, course.getHourlyRate());
            preparedStatement.setString(3, course.getDescription());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        course.setCourseId(generatedKeys.getInt(1));
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Course getCourseById(int id) {
        String query = "SELECT * FROM Courses WHERE course_id = ?";
        Course course = null;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("course_name");
                    float hourly_rate = resultSet.getFloat("hourly_rate");
                    String description = resultSet.getString("description"); // Get the description

                    course = new Course(id, name, hourly_rate, description);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return course;
    }

    public void updateCourse(int course_id, Course course) {
        String query = "UPDATE Courses SET course_name = ?, hourly_rate = ?, description = ? WHERE course_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, course.getName());
            preparedStatement.setFloat(2, course.getHourlyRate());
            preparedStatement.setString(3, course.getDescription()); // Set the description
            preparedStatement.setInt(4, course_id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCourse(int course_id) {
        String deleteEnrollmentsQuery = "DELETE FROM Enrollments WHERE course_id = ?";
        String deleteCourseQuery = "DELETE FROM Courses WHERE course_id = ?";
        
        Connection connection = null;

        try {
            connection = DatabaseConnection.getConnection();
            
            // Begin transaction
            connection.setAutoCommit(false);

            // Step 1: Delete enrollments associated with the course
            try (PreparedStatement deleteEnrollmentsStmt = connection.prepareStatement(deleteEnrollmentsQuery)) {
                deleteEnrollmentsStmt.setInt(1, course_id);
                deleteEnrollmentsStmt.executeUpdate();
            }

            // Step 2: Delete the course
            try (PreparedStatement deleteCourseStmt = connection.prepareStatement(deleteCourseQuery)) {
                deleteCourseStmt.setInt(1, course_id);
                deleteCourseStmt.executeUpdate();
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
