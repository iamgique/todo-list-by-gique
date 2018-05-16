package com.gique.todo.services;

import com.gique.todo.Application;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MessageService {
    private static final Logger log = LogManager.getLogger(Application.class);

    @Autowired
    public MessageService(){

    }

    public TextMessage handleMessage(MessageEvent<TextMessageContent> event) throws Exception {
        log.info("MessageService: handleMessage");
        try {
            Optional.ofNullable(String.valueOf(event.getMessage().getText())).orElseThrow(() -> new Exception());

            if(checkCreateTodoFormat(String.valueOf(event.getMessage().getText()))){
                return new TextMessage("Create todo list");
            }

            if(String.valueOf(event.getMessage().getText()).equals("edit")) {
                return new TextMessage("Edit todo list");
            }

        } catch (Exception e) {
            log.error("Error: {}", e);
        }
        return new TextMessage("https://www.pantip.com");
    }


    public boolean checkCreateTodoFormat(String msg) {
        log.info("checkCreateTodoFormat: {}", msg);
        // \w.*\s:\s\w.*\s:\s\w.*
        String pattern = "\\w.*\\s:\\s\\w.*\\s";
        return String.valueOf(msg).matches(pattern);

    }

}
