package com.courses.management.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/course/*")
public class CourseController {

    private Courses courses;

    @Autowired
    public void setCourses(Courses courses) {
        this.courses = courses;
    }

    @GetMapping(path = "showCourses")
    public String showCourses(Model model) {
        model.addAttribute("courses", courses.showCourses());
        return "show_courses";
    }

    @GetMapping(path = "/get")
    public String getCourse(@RequestParam(name = "id") String id, Model model) {
        model.addAttribute("course", courses.getById(Integer.parseInt(id)));
        return "course_details";
    }

    @GetMapping(path = "/createCourse")
    public String getCreateCourseView(Model model) {
        model.addAttribute("courseStatuses", CourseStatus.values());
        return "create_course";
    }

    @PostMapping(path = "/createCourse")
    public String createCourse(@ModelAttribute("course") @Valid Course course, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("courseStatuses", CourseStatus.values());
            return "create_course";
        }
        try {
            courses.createCourse(course);
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
