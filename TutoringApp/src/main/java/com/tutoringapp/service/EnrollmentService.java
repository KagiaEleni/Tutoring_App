package com.tutoringapp.service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.tutoringapp.config.DatabaseConnection;
import com.tutoringapp.model.Enrollment;

public class EnrollmentService {

    public List<Enrollment> getAllEnrollments() {
        List<Enrollment> enrollments = new ArrayList<>();
        String query = "SELECT * FROM enrollments";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int enrollment_id = resultSet.getInt("enrollment_id");
                int student_id = resultSet.getInt("student_id");
                int course_id = resultSet.getInt("course_id");
                int hours_per_week = resultSet.getInt("hours_per_week");
                float monthly_fee = resultSet.getFloat("monthly_fee");

                enrollments.add(new Enrollment(enrollment_id, student_id, course_id, hours_per_week, monthly_fee));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enrollments;
    }

    public void addEnrollment(Enrollment enrollment) {
        String query = "INSERT INTO enrollments (student_id, course_id, hours_per_week, monthly_fee) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, enrollment.getStudent_id());
            preparedStatement.setInt(2, enrollment.getCourse_id());
            preparedStatement.setInt(3, enrollment.getHours_per_week());
            preparedStatement.setFloat(4, enrollment.getMonthly_fee());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        enrollment.setEnrollment_id(generatedKeys.getInt(1));
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Enrollment getEnrollmentById(int enrollmentId) {
        String query = "SELECT * FROM enrollments WHERE enrollment_id = ?";
        Enrollment enrollment = null;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, enrollmentId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int student_id = resultSet.getInt("student_id");
                    int course_id = resultSet.getInt("course_id");
                    int hours_per_week = resultSet.getInt("hours_per_week");
                    float monthly_fee = resultSet.getFloat("monthly_fee");

                    enrollment = new Enrollment(enrollmentId, student_id, course_id, hours_per_week, monthly_fee);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enrollment;
    }

    public void updateEnrollment(int enrollmentId, Enrollment enrollment) {
        String query = "UPDATE enrollments SET student_id = ?, course_id = ?, hours_per_week = ?, monthly_fee = ? WHERE enrollment_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, enrollment.getStudent_id());
            preparedStatement.setInt(2, enrollment.getCourse_id());
            preparedStatement.setInt(3, enrollment.getHours_per_week());
            preparedStatement.setFloat(4, enrollment.getMonthly_fee());
            preparedStatement.setInt(5, enrollmentId);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteEnrollment(int enrollmentId) {
        String query = "DELETE FROM enrollments WHERE enrollment_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, enrollmentId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
