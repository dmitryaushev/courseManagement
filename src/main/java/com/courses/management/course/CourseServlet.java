package com.courses.management.course;

import com.courses.management.config.DatabaseConnector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/course/*")
public class CourseServlet extends HttpServlet {

    private Courses service;

    @Override
    public void init() throws ServletException {

        super.init();
        service = new Courses(new CourseDAOImpl(DatabaseConnector.getDataSource()));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = getAction(req);
        if (action.startsWith("/showCourse")) {
            List<Course> courses = service.showCourses();
            req.setAttribute("courses", courses);
            req.getRequestDispatcher("/view/showCourses.jsp").forward(req, resp);
        } if (action.startsWith("/get")) {
            String id = req.getParameter("id");
            service.getById(Integer.parseInt(id));

        }
    }

    private String getAction(HttpServletRequest req) {
        String requestURI = req.getRequestURI();
        String requestPathWithServletContext = req.getContextPath() + req.getServletPath();
        return requestURI.substring(requestPathWithServletContext.length());
    }
}

