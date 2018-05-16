package com.gique.todo.controllers;

import com.gique.todo.Application;
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
import org.springframework.stereotype.Controller;

@LineMessageHandler
@Controller
public class MessageController {
    private static final Logger log = LogManager.getLogger(Application.class);

    @NonNull
    private MessageService messageService;

    //@NonNull
    //private TodoListService todoListService;

    @Autowired
    public MessageController(MessageService messageService){
        this.messageService = messageService;
        //this.todoListService = todoListService;
    }

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
        log.info(String.format("Line user event: {}", event));
        try {
            return messageService.handleMessage(event);
        } catch (Exception e) {
            log.error(String.format("Error: {}", e.getMessage()));
            return new TextMessage("System Error... Please try again.");
        }
        //return new TextMessage(event.getMessage().getText());
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }

}
