package com.courses.management.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
        model.addAttribute("user", users.getUser(id));
        return "user_details";
    }

    @GetMapping(path = "/findUser")
    public String showFindUserPage() {
       return "find_user";
    }

    @GetMapping(path = "/find")
    public String findUser(@RequestParam("email") String email, Model model) {
        try {
            model.addAttribute("user", users.getUser(email));
            return "user_details";
        } catch (UserNotExistException e) {
            model.addAttribute("error", e.getMessage());
            return "find_user";
        }
    }

    @GetMapping(path = "/registration")
    public String registration(Model model) {
        return "registration";
    }

    @PostMapping(path = "/registration")
    public String registerUser(@ModelAttribute("userForm") @Valid User user, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "registration";
        }

        try {
            users.registerUser(user);
            return "redirect:login";
        } catch (UserAlreadyExistsException e) {
            model.addAttribute("message", "An account for that username already exists.");
            return "registration";
        }
    }

    @ModelAttribute("userForm")
    public User getDefaultUser() {
        return new User();
    }
}
