package com.tutoringapp.servlets;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tutoringapp.model.Student;
import com.tutoringapp.service.StudentService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/MyStudent/*")
public class MyStudent extends HttpServlet {
    private StudentService studentService = new StudentService();
    private ObjectMapper objectMapper;

    @Override
    public void init() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    // Handle OPTIONS request for preflight
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCorsHeaders(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCorsHeaders(response);

        // Fetch the student data with enrollments
        List<Student> students = studentService.getAllStudentsWithEnrollments();

        // Set response type and encoding
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Write the JSON response
        objectMapper.writeValue(response.getOutputStream(), students);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCorsHeaders(response);
        response.setStatus(HttpServletResponse.SC_OK);

        // Handle update (PUT) request
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 1) {
            int studentId = Integer.parseInt(pathInfo.substring(1));

            Student updatedStudent = objectMapper.readValue(request.getInputStream(), Student.class);
            studentService.updateStudent(studentId, updatedStudent);

        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid student ID");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCorsHeaders(response); // Set CORS headers

        // Extract the student ID from the URL path
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 1) {
            try {
                int studentId = Integer.parseInt(pathInfo.substring(1));
                studentService.deleteStudent(studentId);
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid student ID");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid student ID");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCorsHeaders(response); // Set CORS headers

        // Parse student data from request
        Student newStudent = objectMapper.readValue(request.getInputStream(), Student.class);
        
        // Log the received student data for debugging
        System.out.println("Received student data: " + newStudent);

        studentService.addStudent(newStudent);

        response.setStatus(HttpServletResponse.SC_CREATED);
        objectMapper.writeValue(response.getOutputStream(), newStudent); // Send back the new student with generated ID
    }

    private void setCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }
}