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
import com.tutoringapp.model.TutorCourses;

public class TutorCoursesService {

    public List<TutorCourses> getAllTutorCourses() {
        List<TutorCourses> tutorCourses = new ArrayList<>();
        String query = "SELECT * FROM tutorCourses";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int tutor_course_id = resultSet.getInt("tutor_course_id");
                int tutor_id = resultSet.getInt("tutor_id");
                int course_id = resultSet.getInt("course_id");
                
                tutorCourses.add(new TutorCourses(tutor_course_id, tutor_id, course_id));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tutorCourses;
    }

    public void addTutorCourses(TutorCourses tutorCourses) {
        String query = "INSERT INTO tutorCourses (tutor_id, course_id) VALUES (?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, tutorCourses.getTutor_id());
            preparedStatement.setInt(2, tutorCourses.getCourse_id());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                    	tutorCourses.setTutor_course_id(generatedKeys.getInt(1));
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public TutorCourses getTutorCoursesById(int tutor_course_id) {
        String query = "SELECT * FROM tutorCourses WHERE tutor_course_id = ?";
        TutorCourses tutorCourses = null;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, tutor_course_id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int tutor_id = resultSet.getInt("tutor_id");
                    int course_id = resultSet.getInt("course_id");

                    tutorCourses = new TutorCourses(tutor_course_id, tutor_id, course_id);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tutorCourses;
    }

    public void updateTutorCourses(int tutor_course_id, TutorCourses tutorCourses) {
        String query = "UPDATE tutorCourses SET tutor_id = ?, course_id = ? WHERE tutor_course_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, tutorCourses.getTutor_id());
            preparedStatement.setInt(2, tutorCourses.getCourse_id());
            
            preparedStatement.setInt(3, tutor_course_id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTutorCourses(int tutor_course_id) {
        String query = "DELETE FROM tutorCourses WHERE tutor_course_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, tutor_course_id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
