package com.gique.todo.services;

import com.gique.todo.models.TodoTaskModel;
import com.gique.todo.utils.Util;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class MessageServiceTest {

    @Autowired
    private MessageService messageService;

    @Mock
    private TodoListService todoListService;

    @Mock
    Util util;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        messageService = new MessageService(todoListService, util);
        todoListService = new TodoListService(util);
    }

    @Test
    public void shouldBeReturnModelSuccessWhenCallSplitTodoTask() throws Exception {
        String req = "Task Name : 20/12/18 : 22:22";
        TodoTaskModel responseModel = new TodoTaskModel();
        responseModel.setTask("Task Name");
        responseModel.setDate("20/12/18");
        responseModel.setTime("22:22");

        TodoTaskModel resp = messageService.splitTodoTask(req);
        assertEquals(responseModel.getDate(), resp.getDate());
        assertEquals(responseModel.getTask(), resp.getTask());
        assertEquals(responseModel.getTime(), resp.getTime());
    }

    @Test
    public void shouldBeReturnModelSuccessWhenCallSplitTodoTaskWithTaskAndDateOnly() throws Exception {
        String req = "Task Name : 20/12/18";
        TodoTaskModel responseModel = new TodoTaskModel();
        responseModel.setTask("Task Name");
        responseModel.setDate("20/12/18");
        responseModel.setTime("");

        TodoTaskModel resp = messageService.splitTodoTask(req);
        assertEquals(responseModel.getDate(), resp.getDate());
        assertEquals(responseModel.getTask(), resp.getTask());
        assertEquals(responseModel.getTime(), resp.getTime());
    }

}
