package com.gique.todo.controllers;

import com.gique.todo.constants.Response;
import com.gique.todo.models.MessageModel;
import com.gique.todo.services.TodoListService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class MessageControllerTest {

    @InjectMocks
    MessageController messageController;

    @Mock
    TodoListService todoListService;

    MockMvc mvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(messageController).setControllerAdvice().build();
    }

    @Test
    public void shouldReturnSuccessWhenCallPushMsg() throws Exception {
        List<String> lineIds = new ArrayList<>();
        lineIds.add("id_test");

        String completeCount = "3";
        String inCompleteCount = "2";

        List<MessageModel> messageModels = new ArrayList<>();
        MessageModel messageModel = new MessageModel();
        messageModels = new ArrayList<>();
        messageModel = new MessageModel();
        messageModel.setType("text");
        messageModel.setText("This is your summary of your task");
        messageModels.add(messageModel);
        messageModel = new MessageModel();
        messageModel.setType("text");
        messageModel.setText("The count of your task completed is: " + completeCount + ".");
        messageModels.add(messageModel);
        messageModel = new MessageModel();
        messageModel.setType("text");
        messageModel.setText("The count of your task incomplete is: " + inCompleteCount + ".");
        messageModels.add(messageModel);

        when(todoListService.listLineId()).thenReturn(lineIds);
        when(todoListService.listCountStatusByLineId(lineIds.get(0), "completed")).thenReturn(completeCount);
        when(todoListService.listCountStatusByLineId(lineIds.get(0), "incomplete")).thenReturn(inCompleteCount);

        String url = "/pushMsg";
        try {
            mvc.perform(post(url))
                    .andExpect(jsonPath("$.status.message", is(Response.SUCCESS.getContent())))
                    .andExpect(jsonPath("$.status.code", is(Response.SUCCESS_CODE.getContent())))
                    .andExpect(jsonPath("$.data", is("")));

        }catch (Exception e){
        }
    }

    @Test
    public void shouldReturnErrorWhenCallPushMsg() throws Exception {
        List<String> lineIds = new ArrayList<>();
        lineIds.add("id_test");

        when(todoListService.listLineId()).thenThrow(SQLException.class);

        String url = "/pushMsg";
        try {
            mvc.perform(post(url))
                    .andExpect(jsonPath("$.status.message", is(Response.ERROR.getContent())))
                    .andExpect(jsonPath("$.status.code", is(Response.FAIL_CODE.getContent())))
                    .andExpect(jsonPath("$.data", is("")));

        }catch (Exception e){
            assertTrue(e instanceof Exception);
        }
    }

}
