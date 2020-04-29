package com.courses.management.homework;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.List;

@Controller
@RequestMapping(path = "/homework/*")
public class HomeworkController {

    private Homeworks homeworks;

    @Autowired
    public void setHomeworks(Homeworks homeworks) {
        this.homeworks = homeworks;
    }

    @GetMapping(path = "/upload")
    public String getCreateHomeworkPage(@RequestParam(name = "course_id") String courseId, Model model) {
        model.addAttribute("courseId", courseId);
        return "create_homework";
    }

    @GetMapping(path = "/get")
    public void previewHomework(@RequestParam(name = "id") Integer id, HttpServletResponse response) throws IOException {
        Homework homework = homeworks.get(id);
        File file = new File(homework.getPath());
        if (!file.exists()) {
            throw new FileNotFoundException("No file found");
        }
        response.setHeader("Content-Type", URLConnection.guessContentTypeFromName(file.getName()));
        response.setHeader("Content-Length", String.valueOf(file.length()));
        response.setHeader("Content-Disposition", String.format("inline; filename=\"%s\"", homework.getTitle()));
        Files.copy(file.toPath(), response.getOutputStream());
    }

    @GetMapping(path = "/preview")
    public String getPreviewPage(@RequestParam(name = "id") String id, Model model) {
        model.addAttribute("homeworkId", id);
        return "preview_homework";
    }

    @PostMapping(path = "/upload")
    public ModelAndView uploadHomework(@RequestParam(name = "course_id") Integer courseId,
                                       HttpServletRequest request, ModelMap model) {
        if (ServletFileUpload.isMultipartContent(request)) {
            try {
                List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
                homeworks.uploadFile(multiparts, courseId);
            } catch (Exception e) {
                model.addAttribute("error", "File upload failed dues to " + e);
                return new ModelAndView("create_homework", model);
            }
        } else {
            model.addAttribute("error", "No file found");
            return new ModelAndView("create_homework", model);
        }
        return new ModelAndView(String.format("redirect:/course/get?id=%s", courseId));
    }

    @ExceptionHandler({FileNotFoundException.class})
    public ModelAndView handleException(FileNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("file_not_found");
        modelAndView.addObject("error", e.getMessage());
        modelAndView.setStatus(HttpStatus.BAD_REQUEST);
        return modelAndView;
    }
}