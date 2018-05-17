package com.gique.todo.utils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UtilTest {
    @Autowired
    Util util;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        util = new Util();
    }

    @Test
    public void shouldReturnBooleanWhenCheckCreateTodoFormat() throws Exception {
        assertEquals(util.checkCreateTodoFormat("Task Name : 20/12/18 : 22:22"), true);
        assertEquals(util.checkCreateTodoFormat("Task Name : today : 22:22"), true);
        assertEquals(util.checkCreateTodoFormat("Task Name : tomorrow : 22:22"), true);
        assertEquals(util.checkCreateTodoFormat("Task Name : 20/12/18"), true);
        assertEquals(util.checkCreateTodoFormat("Task Name : today"), true);
        assertEquals(util.checkCreateTodoFormat("Task Name : tomorrow"), true);
        assertEquals(util.checkCreateTodoFormat("Task Name : 20/12/18 : aaaaa"), false);
        assertEquals(util.checkCreateTodoFormat("Task Name : 20/12/2018 : 22:22"), false);
        assertEquals(util.checkCreateTodoFormat("Task Name"), false);
        assertEquals(util.checkCreateTodoFormat(""), false);
    }

    @Test
    public void shouldReturnRtightFormatWhenGetDueDateRequestDateOnly() throws Exception {
        String reqDate = "20/12/18";
        String reqTime = "";
        String resp = util.getDueDate(reqDate, reqTime);
        assertEquals(resp, "2018-12-20 12:00");
    }

    @Test
    public void shouldReturnRtightFormatWhenGetDueDateRequestDateAndTime() throws Exception {
        String reqDate = "20/12/18";
        String reqTime = "30:20";
        String resp = util.getDueDate(reqDate, reqTime);
        assertEquals(resp, "2018-12-20 30:20");
    }

    @Test
    public void shouldReturnExceptionWhenGetDueDateRequestDateIsWrong() {
        String reqDate = "abcd";
        String reqTime = "";
        try {
            String resp = util.getDueDate(reqDate, reqTime);
        } catch (Exception e) {
            assertTrue(e instanceof ParseException);
        }
    }
}
