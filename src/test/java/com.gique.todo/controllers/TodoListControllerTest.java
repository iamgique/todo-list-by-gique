package com.gique.todo.controllers;

import com.gique.todo.constants.Response;
import com.gique.todo.models.TodoModel;
import com.gique.todo.services.TodoListService;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class TodoListControllerTest {

    @InjectMocks
    TodoListController todoListController;

    @Mock
    TodoListService todoListService;

    MockMvc mvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(todoListController).setControllerAdvice().build();
    }

    @Test
    public void shouldBeReturnSuccessWhenGetList() throws Exception {
        String lineId = "test_line_id";
        List<TodoModel> todoModels = new ArrayList<>();
        TodoModel todoModel = new TodoModel();
        todoModel.setId(1);
        todoModel.setLineId("test_line_id");
        todoModel.setStatus("incomplete");
        todoModel.setImportant("0");
        todoModel.setDueDate("2018-10-10 18:30");
        todoModel.setCreatedAt("2018-10-10 12:30");
        todoModel.setUpdatedAt("2018-10-10 12:30");
        todoModels.add(todoModel);

        when(todoListService.getTodoTaskByLineId(lineId)).thenReturn(todoModels);

        String url = "/todolist/v1/list";
        try {
            mvc.perform(get(url)
                    .param("line_id", "test_line_id"))
                    .andExpect(jsonPath("$.status.message", is(Response.SUCCESS.getContent())))
                    .andExpect(jsonPath("$.status.code", is(Response.SUCCESS_CODE.getContent())));

        }catch (Exception e){
        }

    }

}
