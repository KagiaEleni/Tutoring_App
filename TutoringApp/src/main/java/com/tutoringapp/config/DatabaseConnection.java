package com.tutoringapp.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
	
	private static final String URL = "jdbc:postgresql://localhost:5432/tutoring_app";
    private static final String USER = "eleni";
    private static final String PASSWORD = "p216iu5j";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to the PostgreSQL database successfully!");
        } catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }
        return connection;
    }
}

