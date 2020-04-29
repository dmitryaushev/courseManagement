package com.courses.management.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path = "/user/*")
public class UserController {

    private Users users;

    @Autowired
    public void setUsers(Users users) {
        this.users = users;
    }

    @GetMapping(path = "/showUsers")
    public String getAllUsers(Model model) {
        model.addAttribute("users", users.getAll());
        return "show_users";
    }

    @GetMapping(path = "/get")
    public String getUser(@RequestParam("id") Integer id, Model model) {
        model.addAttribute("user", users.getById(id));
        return "user_details";
    }

    @GetMapping(path = "/findUser")
    public String showFindUserPage() {
       return "find_user";
    }

    @GetMapping(path = "/find")
    public String findUser(@RequestParam("email") String email, Model model) {
        try {
            model.addAttribute("user", users.getByEmail(email));
            return "user_details";
        } catch (UserNotExistError e) {
            model.addAttribute("error", e.getMessage());
            return "find_user";
        }
    }
}
