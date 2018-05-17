package com.gique.todo.services;

import com.gique.todo.Application;
import com.gique.todo.models.TodoTaskModel;
import com.gique.todo.utils.Util;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
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

    @NonNull
    private final Util util;

    @Autowired
    public MessageService(Util util){
        this.util = util;
    }

    public void saveTodoTask(TodoTaskModel todoTaskModel) throws SQLException, ParseException {
        String dueDate = util.getDueDate(todoTaskModel.getDate(), todoTaskModel.getTime());
        Statement stmt = dataSource.getConnection().createStatement();
        stmt.executeUpdate("INSERT INTO todo (line_id, task, status, important, due_date, created_at, updated_at) " +
                "VALUES ('"+todoTaskModel.getLineId()+"', '"+todoTaskModel.getTask()+"', 'incomplete', '0', '"+dueDate+"', now(), now());");
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
                return new TextMessage("Todo List \n Your Task: "
                    + todoTaskModel.getTask() + " : " + util.getDueDate(todoTaskModel.getDate(), todoTaskModel.getTime()));
            } else if (msg.equals("edit")) {
                return new TextMessage("Edit todo list");
            } else {
                return new TextMessage("For example make todo list \n task : date/month/year : time e.g Buy milk : 3/5/18 : 13:00 \n task : today : time e.g Finsh writing shopping list : today : 15:30 \n task : tommorrow : time e.g Watch movie : tommorrow : 18:00 \n ** If not specific time then use default time as 12:00 pm ** \n ** todo list by input word edit in chat and system will return webview **");
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
