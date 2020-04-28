package com.courses.management.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/user/*")
public class UserController {

    private Users users;

    @Autowired
    public void setUsers(Users users) {
        this.users = users;
    }

    @GetMapping(path = "/get")
    public String getUser(String id, Model model) {
        model.addAttribute("user", users.getById(Integer.parseInt(id)));
        return "user_details";
    }

    @GetMapping(path = "/findUser")
    public String getFindUserView() {
       return "find_user";
    }

    @PostMapping(path = "/findUser")
    public String findUser(@ModelAttribute("user") User user, Model model) {
        try {
            model.addAttribute("user", users.getByEmail(user.getEmail()));
            return "user_details";
        } catch (UserNotExistError e) {
            model.addAttribute("error", e.getMessage());
            return "find_user";
        }
    }

    @ModelAttribute("user")
    public User getDefaultUser() {
        return new User();
    }
}
