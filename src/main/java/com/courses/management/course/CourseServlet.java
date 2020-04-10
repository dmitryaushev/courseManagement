package com.courses.management.course;

import com.courses.management.common.Validator;
import com.courses.management.common.exceptions.ErrorMessage;
import com.courses.management.config.HibernateDatabaseConnector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@WebServlet("/course/*")
public class CourseServlet extends HttpServlet {

    private Courses service;

    @Override
    public void init() throws ServletException {

        super.init();
        service = new Courses(new CourseDAOImpl(HibernateDatabaseConnector.getSessionFactory()));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = getAction(req);
        if (action.startsWith("/showCourse")) {
            List<Course> courses = service.showCourses();
            req.setAttribute("courses", courses);
            req.getRequestDispatcher("/view/showCourses.jsp").forward(req, resp);
        } else if (action.startsWith("/get")) {
            String id = req.getParameter("id");
            Course course = service.getById(Integer.parseInt(id));
            req.setAttribute("course", course);
            req.getRequestDispatcher("/view/course_details.jsp").forward(req, resp);
        } else if (action.startsWith("/createCourse")) {
            req.setAttribute("courseStatuses", CourseStatus.values());
            req.getRequestDispatcher("/view/create_course.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = getAction(req);
        if (action.startsWith("/createCourse")) {
            Course course = mapCourse(req);
            List<ErrorMessage> errorMessages = validateCourse(course);
            if (!errorMessages.isEmpty()) {
                req.setAttribute("errors", errorMessages);
                req.setAttribute("courseStatuses", CourseStatus.values());
                req.getRequestDispatcher("/view/create_course.jsp").forward(req, resp);
            } else {
                service.createCourse(course);
                req.setAttribute("courseTitle", course.getTitle());
                req.getRequestDispatcher("/view/course_created.jsp").forward(req, resp);
            }
        }
    }

    private List<ErrorMessage> validateCourse(Course course) {

        List<ErrorMessage> errorMessages = Validator.validateEntity(course);
        Course persistentCourse = service.getByTitle(course.getTitle());
        if (Objects.nonNull(persistentCourse)) {
            errorMessages.add(new ErrorMessage(
                    "", String.format("course with title %s already exists", persistentCourse.getTitle())));
        }
        return errorMessages;
    }

    private Course mapCourse(HttpServletRequest req) {

        String courseTitle = req.getParameter("title").trim();
        String courseStatusName = req.getParameter("course_status");
        CourseStatus courseStatus = CourseStatus.getCourseStatus(courseStatusName).get();

        Course course = new Course();
        course.setTitle(courseTitle);
        course.setCourseStatus(courseStatus);
        return course;
    }

    private String getAction(HttpServletRequest req) {
        String requestURI = req.getRequestURI();
        String requestPathWithServletContext = req.getContextPath() + req.getServletPath();
        return requestURI.substring(requestPathWithServletContext.length());
    }

}

