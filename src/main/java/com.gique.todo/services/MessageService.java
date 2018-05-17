package com.gique.todo.services;

import com.gique.todo.Application;
import com.gique.todo.models.TodoTaskModel;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class MessageService {
    private static final Logger log = LogManager.getLogger(Application.class);

    @Autowired
    private DataSource dataSource;

    @Autowired
    public MessageService(){
    }

    public void saveTodoTask(TodoTaskModel todoTaskModel) throws SQLException, ParseException {
        String createdAt = getCreatedAt(todoTaskModel);
        Statement stmt = dataSource.getConnection().createStatement();
        stmt.executeUpdate("INSERT INTO todo (line_id, task, status, important, created_at, updated_at) " +
                "VALUES ('"+todoTaskModel.getLineId()+"', '"+todoTaskModel.getTask()+"', 'incomplete', '0', '"+createdAt+"', now());");

    }

    public String getCreatedAt(TodoTaskModel todoTaskModel) throws ParseException {
        String createdAt = "";
        SimpleDateFormat sdf = new SimpleDateFormat("d/M/yy");
        Date d = sdf.parse(todoTaskModel.getDate());

        if(todoTaskModel.getTime() == null || todoTaskModel.getTime().equals("")){
            sdf.applyPattern("yyyy-MM-dd 12:00");
            createdAt = sdf.format(d);
        } else {
            sdf.applyPattern("yyyy-MM-dd" + todoTaskModel.getTime());
            createdAt = sdf.format(d);
        }
        return createdAt;
    }

    public TextMessage handleMessage(MessageEvent<TextMessageContent> event) throws Exception {
        log.info("MessageService: handleMessage");
        try {
            Optional.ofNullable(String.valueOf(event.getSource().getUserId())).orElseThrow(() -> new Exception());
            Optional.ofNullable(String.valueOf(event.getMessage().getText())).orElseThrow(() -> new Exception());
            String msg = String.valueOf(event.getMessage().getText());

            if (checkCreateTodoFormat(msg)) {
                TodoTaskModel todoTaskModel = splitTodoTask(msg);
                todoTaskModel.setLineId(String.valueOf(event.getSource().getUserId()));
                saveTodoTask(todoTaskModel);
                return new TextMessage("Your Todo List \n "
                    + todoTaskModel.getTask() + " : " + getCreatedAt(todoTaskModel));
            } else if (msg.equals("edit")) {
                return new TextMessage("Edit todo list");
            } else {
                return new TextMessage("Cannot post Todo task because it's wrong format.");
            }

        } catch (Exception e) {
            log.error("Error: {}", e);
            return new TextMessage("Cannot post Todo task because it's wrong format.");
        }
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
        String regex = "(.*)\\s:\\s(\\d{1,2}/\\d{1,2}/\\d{1,2}|today|tomorrow)(\\s:\\s\\d{1,2}:\\d{1,2}|$)";
        //String regex = "(\\w.*\\s:\\s)[^:]\\w.*";
        //String regex = "/.*\\s:\\s.*\\s/g";
        return Pattern.matches(regex, msg);
    }

    /*public static void main(String args[]) throws SQLException, ParseException {
        saveTodoTask();
        *//*String a = "aaaa : 20/12/18";
        String b = "aaaa : 20/12/18 : dssadkl";
        String c = "aaaa : 20/12/18 : 22:22";
        String d = "aaaaaa";
        String e = "aaa@aaa.com";
        String f = "aaaa : : 20/12/18 : 22:22";
        String g = "aaaa vvvv : 20/12/18 : 22:22";

        System.err.println(checkCreateTodoFormat(a));
        System.err.println(checkCreateTodoFormat(b));
        System.err.println(checkCreateTodoFormat(c));
        System.err.println(checkCreateTodoFormat(d));
        System.err.println(checkCreateTodoFormat(e));
        System.err.println(checkCreateTodoFormat(f));
        System.err.println(checkCreateTodoFormat(g));*//*
    }*/

}
