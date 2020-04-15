package com.courses.management.user;

import com.courses.management.common.exceptions.ErrorMessage;
import com.courses.management.config.HibernateDatabaseConnector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@WebServlet("/user/*")
public class UserServlet extends HttpServlet {

    private Users service;

    @Override
    public void init() throws ServletException {
        super.init();
        service = new Users(new UserDAOImpl(HibernateDatabaseConnector.getSessionFactory()));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = getAction(req);
        if (action.startsWith("/get")) {
            String id = req.getParameter("id");
            User user = service.getById(Integer.parseInt(id));
            req.setAttribute("user", user);
            req.getRequestDispatcher("/view/user_details.jsp").forward(req, resp);
        } else if (action.startsWith("/findUser")) {
            req.getRequestDispatcher("/view/find_user.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = getAction(req);
        if (action.startsWith("/findUser")) {
            String email = req.getParameter("email");
            User user = service.getByEmail(email);
            List<ErrorMessage> errors = validateUser(email);
            if (!errors.isEmpty()) {
                req.setAttribute("errors", errors);
                req.getRequestDispatcher("/view/find_user.jsp").forward(req, resp);
            } else {
                req.setAttribute("user", user);
                req.getRequestDispatcher("/view/user_details.jsp").forward(req, resp);
            }
        }
    }

    private String getAction(HttpServletRequest req) {
        String requestURI = req.getRequestURI();
        String requestPathWithServletContext = req.getContextPath() + req.getServletPath();
        return requestURI.substring(requestPathWithServletContext.length());
    }

    private List<ErrorMessage> validateUser(String email) {

        List<ErrorMessage> errors = new ArrayList<>();
        if (Objects.isNull(service.getByEmail(email))) {
            errors.add(new ErrorMessage("", String.format("User with email %s not exist", email)));
        }
        return errors;
    }
}
