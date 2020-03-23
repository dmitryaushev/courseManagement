package com.courses.management.cource;

import com.courses.management.common.Command;
import com.courses.management.common.View;
import com.courses.management.common.commands.util.InputString;
import com.courses.management.course.Course;
import com.courses.management.course.CourseDAO;
import com.courses.management.course.CreateCourse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class CreateCourseTest {

    private Command command;
    private CourseDAO courseDAO;
    private View view;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() {
        this.view = mock(View.class);
        this.courseDAO = mock(CourseDAO.class);
        this.command = new CreateCourse(view, courseDAO);
    }

    @Test
    public void testCanProcessWithCorrectCommand() {
        //given
        InputString inputString = new InputString("create_course|JAVA");
        //when
        boolean result = command.canProcess(inputString);
        //then
        assertTrue(result);
    }

    @Test
    public void testCanNotProcessWithWrongCommand() {
        //given
        InputString inputString = new InputString("create|JAVA");
        //when
        boolean result = command.canProcess(inputString);
        //then
        assertFalse(result);
    }

    @Test
    public void testProcessWithAlreadyExistTitle() {
        //given
        Course course = CoursesTest.getTesCourse();
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Course with title JAVA already exists");
        //when
        InputString inputString = new InputString("create_course|JAVA");
        when(courseDAO.get("JAVA")).thenReturn(course);
        command.process(inputString);
    }

    @Test
    public void testProcessWithCorrectParameters() {
        //given
        Course course = CoursesTest.getTesCourse();
        InputString inputString = new InputString(String.format("create_course|%s", course.getTitle()));
        //when
        when(courseDAO.get("JAVA")).thenReturn(null);
        command.process(inputString);
        //then
        verify(view).write(String.format("Course created with title - %s", course.getTitle()));
        verify(courseDAO, times(1)).create(course);
    }

    @Test
    public void testCanNotProcessWithEmptyTitle() {
        //given
        InputString inputString = new InputString("create_course| |");
        //when
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Course title can't be empty");
        when(courseDAO.get("JAVA")).thenReturn(null);
        command.process(inputString);
    }
}
