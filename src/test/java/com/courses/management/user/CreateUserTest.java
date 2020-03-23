package com.courses.management.user;

import com.courses.management.common.Command;
import com.courses.management.common.View;
import com.courses.management.common.commands.util.InputString;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class CreateUserTest {

    private UserDAO userDAO;
    private Command command;
    private View view;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() {
        this.view = mock(View.class);
        this.userDAO = mock(UserDAO.class);
        this.command = new CreateUser(view, userDAO);
    }

    @Test
    public void testCanProcessWithCorrectCommand() {
        //given
        InputString inputString = new InputString("create_user|Dmitry|Aushev|dmitryaushev@gmail.com");
        //when
        boolean result = command.canProcess(inputString);
        //then
        assertTrue(result);
    }

    @Test
    public void testCanProcessWithIncorrectCommand() {
        //given
        InputString inputString = new InputString("create_users|Dmitry|Aushev|dmitryaushev@gmail.com");
        //when
        boolean result = command.canProcess(inputString);
        //then
        assertFalse(result);
    }

    @Test
    public void testProcessWithIncorrectEmail() {
        //given
        InputString inputString = new InputString("create_user|Dmitry|Aushev|dmitryaushev@gmail");
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Wrong email address dmitryaushev@gmail");
        //when
        command.process(inputString);
        //then
    }

    @Test
    public void testProcessWithDuplicateEmail() {
        //given
        InputString inputString = new InputString("create_user|Dmitry|Aushev|dmitryaushev@gmail.com");
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("User with email dmitryaushev@gmail.com already exists");
        //when
        when(userDAO.get("dmitryaushev@gmail.com")).thenReturn(new User());
        command.process(inputString);
    }

    @Test
    public void testProcessWithCorrectData() {
        //given
        InputString inputString = new InputString("create_user|Dmitry|Aushev|dmitryaushev@gmail.com");
        User user = UsersTest.getTestUser();
        //when
        when(userDAO.get("dmitryaushev@gmail.com")).thenReturn(null);
        command.process(inputString);
        //then
        verify(userDAO, times(1)).create(user);
        verify(userDAO, times(1)).get("dmitryaushev@gmail.com");
        verify(view).write(String.format("User %s %s created!", user.getFirstName(), user.getLastName()));
    }

    @Test
    public void testProcessEmptyFirstName() {
        //given
        InputString inputString = new InputString("create_user||Aushev|dmitryaushev@gmail.com");
        User user = UsersTest.getTestUser();
        user.setFirstName(null);
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("User first name can't be empty");
        //when
        command.process(inputString);
    }

    @Test
    public void testProcessEmptyLastName() {
        //given
        InputString inputString = new InputString("create_user|Dmitry||dmitryaushev@gmail.com");
        User user = UsersTest.getTestUser();
        user.setLastName(null);
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("User last name can't be empty");
        //when
        command.process(inputString);
    }

    @Test
    public void testProcessEmptyEmail() {
        //given
        InputString inputString = new InputString("create_user|Dmitry|Aushev| |");
        User user = UsersTest.getTestUser();
        user.setEmail(null);
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Wrong email address");
        //when
        command.process(inputString);
    }
}
