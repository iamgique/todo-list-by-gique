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
        // \w.*\s:\s\w.*\s:\s\w.*
        String pattern = "\\w.*\\s:\\s\\w.*\\s";
        try {
            if(!Optional.ofNullable(String.valueOf(event.getMessage().getText())).equals("")){
                boolean a = String.valueOf(event.getMessage().getText()).matches(pattern);
                log.info(String.format("handleMessage: {}", a));
                return new TextMessage("https://www.apple.com");
            }

            if((Optional.ofNullable(String.valueOf(event.getMessage().getText())).orElse("")).equals("edit")) {
                return new TextMessage("https://www.google.com");
            }
        } catch (Exception e) {
            log.error(String.format("Error: {}", e));
        }

        return new TextMessage("https://www.pantip.com");
    }

}
