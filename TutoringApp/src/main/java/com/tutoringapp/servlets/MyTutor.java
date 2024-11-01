package com.tutoringapp.servlets;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tutoringapp.model.Tutor;
import com.tutoringapp.service.TutorService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/MyTutor/*")
public class MyTutor extends HttpServlet {
    private TutorService tutorService = new TutorService();
    private ObjectMapper objectMapper;

    @Override
    public void init() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }
    
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCorsHeaders(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set CORS headers
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");

        List<Tutor> tutors = tutorService.getAllTutorsWithCourses();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        objectMapper.writeValue(response.getOutputStream(), tutors);
    }
    
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCorsHeaders(response);
        response.setStatus(HttpServletResponse.SC_OK);

        String pathInfo = request.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 1) {
            int tutorId = Integer.parseInt(pathInfo.substring(1));

            Tutor updatedTutor = objectMapper.readValue(request.getInputStream(), Tutor.class);
            tutorService.updateTutor(tutorId, updatedTutor);

        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid tutor ID");
        }
    }
    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCorsHeaders(response); // Set CORS headers

        String pathInfo = request.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 1) {
            try {
                int tutorId = Integer.parseInt(pathInfo.substring(1));
                tutorService.deleteTutor(tutorId);
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid tutor ID");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid tutor ID");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCorsHeaders(response); // Set CORS headers

        Tutor newTutor = objectMapper.readValue(request.getInputStream(), Tutor.class);
        
        System.out.println("Received tutor data: " + newTutor);

        tutorService.addTutor(newTutor);

        response.setStatus(HttpServletResponse.SC_CREATED);
        objectMapper.writeValue(response.getOutputStream(), newTutor);
    }
    
    private void setCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }
}