package com.gique.todo;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Optional;


@SpringBootApplication
@EnableAutoConfiguration
@LineMessageHandler
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        System.out.println("event: " + event);
        System.out.println("Reply Token: " + event.getReplyToken());
        System.out.println("Source: " + event.getSource());
        System.out.println("Timestamp: " + event.getTimestamp());
        System.out.println("Message: " + event.getMessage());

        String msg = Optional.ofNullable(String.valueOf(event.getMessage())).orElse("");
        if(msg.equals("edit")){
            return new TextMessage("https://www.google.com");
        }
        return new TextMessage(event.getMessage().getText());
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }

}
