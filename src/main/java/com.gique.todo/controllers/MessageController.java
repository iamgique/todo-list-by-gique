package com.gique.todo.controllers;

import com.gique.todo.Application;
import com.gique.todo.constants.Constants;
import com.gique.todo.constants.Response;
import com.gique.todo.models.MessageModel;
import com.gique.todo.models.ResponseModel;
import com.gique.todo.models.StatusModel;
import com.gique.todo.services.ExternalService;
import com.gique.todo.services.MessageService;
import com.gique.todo.services.TodoListService;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@LineMessageHandler
@Controller
public class MessageController {
    private static final Logger log = LogManager.getLogger(Application.class);

    @NonNull
    private MessageService messageService;

    @NonNull
    private TodoListService todoListService;

    @NonNull
    private ExternalService externalService;

    @Autowired
    public MessageController(MessageService messageService, TodoListService todoListService, ExternalService externalService){
        this.messageService = messageService;
        this.todoListService = todoListService;
        this.externalService = externalService;
    }

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
        log.info("Line user event: {}", event);
        try {
            return messageService.handleMessage(event);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return new TextMessage(Constants.CANNOT_POST_TODO_LIST.getContent());
        }
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }

    @RequestMapping(value = "/pushMsg", method = RequestMethod.POST)
    public HttpEntity<ResponseModel> pushMsg() throws Exception {
        log.info("pushMsg");
        HttpEntity<ResponseModel> responseModel = null;
        List<MessageModel> messageModels = new ArrayList<>();
        MessageModel messageModel = new MessageModel();

        try {
            List<String> lineIds = todoListService.listLineId();
            Optional.ofNullable(lineIds).orElseThrow(() -> new Exception());
            for(String lineId : lineIds){

                String completed = todoListService.listCountStatusByLineId(lineId, "completed");
                String incomplete = todoListService.listCountStatusByLineId(lineId, "incomplete");

                messageModels = new ArrayList<>();
                messageModel = new MessageModel();
                messageModel.setType("text");
                messageModel.setText("This is your summary of your task");
                messageModels.add(messageModel);
                messageModel = new MessageModel();
                messageModel.setType("text");
                messageModel.setText("The count of your task completed is: " + completed + ".");
                messageModels.add(messageModel);
                messageModel = new MessageModel();
                messageModel.setType("text");
                messageModel.setText("The count of your task incomplete is: " + incomplete + ".");
                messageModels.add(messageModel);

                externalService.pushMsg(lineId, messageModels);
            }


            StatusModel status = new StatusModel(Response.SUCCESS_CODE.getContent(),Response.SUCCESS.getContent());
            responseModel = new HttpEntity<>(new ResponseModel(status, ""));
        } catch (Exception e) {
            log.error(e);
            responseModel = new HttpEntity<>(new ResponseModel(new StatusModel(Response.FAIL_CODE.getContent(), Response.ERROR.getContent()), ""));
        }
        return responseModel;
    }

}
