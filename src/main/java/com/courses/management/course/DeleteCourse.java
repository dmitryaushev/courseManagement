package com.courses.management.course;

import com.courses.management.common.Command;
import com.courses.management.common.View;
import org.apache.commons.lang3.math.NumberUtils;


public class DeleteCourse implements Command {

    private View view;
    private CourseDAOImpl courseDAO;

    public DeleteCourse(View view) {
        this.view = view;
        courseDAO = new CourseDAOImpl();
    }

    @Override
    public String command() {
        return "delete_course";
    }

    @Override
    public void process() {

        view.write("Enter id or title");
        String input = view.read();

        if(NumberUtils.isNumber(input))
            courseDAO.delete(Integer.parseInt(input));
        else
            courseDAO.delete(input);

        view.write("Course deleted");
    }
}
