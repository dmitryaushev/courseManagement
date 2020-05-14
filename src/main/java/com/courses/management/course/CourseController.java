package com.courses.management.course;

import com.courses.management.user.User;
import com.courses.management.user.UserRole;
import com.courses.management.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping(path = "/course/*")
public class CourseController {

    private CourseService courseService;
    private UserService userService;

    @Autowired
    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "showCourses")
    public String showCourses(Model model) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        GrantedAuthority authority = userDetails.getAuthorities().iterator().next();
        if (authority.getAuthority().equals(UserRole.ROLE_ADMIN.getRole())) {
            model.addAttribute("courses", courseService.showCourses());
        } else {
            User user = userService.getUser(userDetails.getUsername());
            if (Objects.nonNull(user.getCourse())) {
                model.addAttribute("courses", List.of(user.getCourse()));
            }
        }
        return "show_courses";
    }

    @GetMapping(path = "/get")
    public String getCourse(@RequestParam(name = "id") String id, Model model) {
        model.addAttribute("course", courseService.getById(Integer.parseInt(id)));
        return "course_details";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/createCourse")
    public String getCreateCourseView(Model model) {
        model.addAttribute("courseStatuses", CourseStatus.values());
        return "create_course";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/createCourse")
    public String createCourse(@ModelAttribute("course") @Valid Course course, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("courseStatuses", CourseStatus.values());
            return "create_course";
        }
        try {
            courseService.createCourse(course);
            model.addAttribute("course_title", course.getTitle());
            return "course_created";
        } catch (CourseAlreadyExistError e) {
            model.addAttribute("courseStatuses", CourseStatus.values());
            model.addAttribute("error", e.getMessage());
            return "create_course";
        }

    }

    @ModelAttribute("course")
    public Course getDefaultCourse() {
        return new Course();
    }
}
