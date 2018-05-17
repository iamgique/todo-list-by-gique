package com.gique.todo.controllers;

import com.gique.todo.services.MessageService;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class MessageControllerTest {

    @InjectMocks
    MessageController messageController;

    @Mock
    MessageService messageService;

    MockMvc mvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(messageController).setControllerAdvice().build();
    }

    /*@Test
    public void handleTextMessageEventSuccess() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Robot");
        map.put("description", "RobotTest");

        when(clientDetailsService.getClientInfo(clientId, false)).thenReturn(map);

        String url = "/resource/client/v1/" + clientId;

        mvc.perform(get(url))
                .andExpect(jsonPath("$.name", is("Robot")))
                .andExpect(jsonPath("$.description", is("RobotTest")))
                .andExpect(status().isOk());

    }*/

}
