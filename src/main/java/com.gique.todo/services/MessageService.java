package com.gique.todo.services;

import com.gique.todo.Application;
import com.gique.todo.models.ResponseModel;
import com.gique.todo.models.TodoTaskModel;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class MessageService {
    private static final Logger log = LogManager.getLogger(Application.class);

    @Autowired
    private UserService userService;

    @Autowired
    public MessageService(UserService userService){
        this.userService = userService;
    }

    public TextMessage handleMessage(MessageEvent<TextMessageContent> event) throws Exception {
        log.info("MessageService: handleMessage");
        try {
            Optional.ofNullable(String.valueOf(event.getSource().getUserId())).orElseThrow(() -> new Exception());
            Optional.ofNullable(String.valueOf(event.getMessage().getText())).orElseThrow(() -> new Exception());
            String userId = event.getSource().getUserId();
            String msg = String.valueOf(event.getMessage().getText());

            if(checkCreateTodoFormat(msg)){
                TodoTaskModel todoTaskModel = splitTodoTask(msg);
                return new TextMessage("User ID: "+ userId + "\n" + userService.getUserProfile(userId) +
                        "Create todo list: \n " + todoTaskModel.getTask() + " : " + todoTaskModel.getDate() +
                        " : " + todoTaskModel.getTime());
            }

            if(msg.equals("edit")) {
                return new TextMessage("Edit todo list");
            }

        } catch (Exception e) {
            log.error("Error: {}", e);
        }
        return new TextMessage("https://www.pantip.com");
    }


    public TodoTaskModel splitTodoTask(String msg){
        log.info("splitTodoTask: {}", msg);
        TodoTaskModel responseModel = new TodoTaskModel();
        String[] sp = msg.split("\\s:\\s");
        if(sp != null){
            responseModel.setTask((sp.length > 0 ? sp[0] : ""));
            responseModel.setDate((sp.length > 1 ? sp[1] : ""));
            responseModel.setTime((sp.length > 2 ? sp[2] : ""));
        }
        return responseModel;
    }

    public boolean checkCreateTodoFormat(String msg) {
        log.info("checkCreateTodoFormat: {}", msg);
        String regex = "(\\w.*\\s:\\s)[^:]\\w.*";
        //String regex = "/.*\\s:\\s.*\\s/g";
        //String regex = "/^([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})$/";
        //Pattern pattern = Pattern.compile(regex);
        return Pattern.matches(regex, msg);
    }

    /*public static void main(String args[]){
        String a = "aaaa : 20/12/18";
        String b = "aaaa : 20/12/18 : dssadkl";
        String c = "aaaa : 20/12/18 : 22:22";
        String d = "aaaaaa";
        String e = "aaa@aaa.com";
        String f = "aaaa : : 20/12/18 : 22:22";
        String g = "aaaa vvvv : 20/12/18 : 22:22";

        System.err.println(splitTodoTask(a));
        System.err.println(splitTodoTask(b));
        System.err.println(splitTodoTask(c));
        System.err.println(splitTodoTask(d));
        System.err.println(splitTodoTask(e));
        System.err.println(splitTodoTask(f));
        System.err.println(splitTodoTask(g));
    }*/

}
