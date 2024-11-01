package com.tutoringapp.servlets;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tutoringapp.model.TutorCourses;
import com.tutoringapp.service.TutorCoursesService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/MyTutorCourses/*")
public class MyTutorCourses extends HttpServlet {
    private TutorCoursesService TutorCoursesService = new TutorCoursesService();
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

        List<TutorCourses> tutorCourses = TutorCoursesService.getAllTutorCourses();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        objectMapper.writeValue(response.getOutputStream(), tutorCourses);
    }
    
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCorsHeaders(response);
        response.setStatus(HttpServletResponse.SC_OK);

        String pathInfo = request.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 1) {
            int tutor_courseId = Integer.parseInt(pathInfo.substring(1));

            TutorCourses tutorCourses = objectMapper.readValue(request.getInputStream(), TutorCourses.class);
            TutorCoursesService.updateTutorCourses(tutor_courseId, tutorCourses);

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
                int tutor_courseId = Integer.parseInt(pathInfo.substring(1));
                TutorCoursesService.deleteTutorCourses(tutor_courseId);
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

        TutorCourses newTutorCourses = objectMapper.readValue(request.getInputStream(), TutorCourses.class);
        
        System.out.println("Received TutorCourses data: " + newTutorCourses);

        TutorCoursesService.addTutorCourses(newTutorCourses);

        response.setStatus(HttpServletResponse.SC_CREATED);
        objectMapper.writeValue(response.getOutputStream(), newTutorCourses);
    }
    
    private void setCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }
}