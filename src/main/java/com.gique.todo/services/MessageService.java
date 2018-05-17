package com.gique.todo.services;

import com.gique.todo.models.TodoTaskModel;
import com.gique.todo.utils.Util;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.Optional;

@Service
public class MessageService {
    private static final Logger log = LogManager.getLogger(MessageService.class);

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

    public boolean getTodoTaskByLineId(String lineId) throws SQLException {
        Statement stmt = dataSource.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("SELECT task, status, important, due_date FROM todo " +
                "WHERE line_id = '"+lineId+"' ORDER BY important, due_date ASC");

        while (rs.next()) {
            System.out.println("Read from DB: " + rs.getString("task"));
            System.out.println("Read from DB: " + rs.getString("status"));
            System.out.println("Read from DB: " + rs.getString("important"));
            System.out.println("Read from DB: " + rs.getString("due_date"));
        }
        return true;
    }

    public TextMessage handleMessage(MessageEvent<TextMessageContent> event) throws Exception {
        log.info("MessageService: handleMessage");
        try {
            Optional.ofNullable(String.valueOf(event.getSource().getUserId())).orElseThrow(() -> new Exception());
            Optional.ofNullable(String.valueOf(event.getMessage().getText())).orElseThrow(() -> new Exception());
            String msg = String.valueOf(event.getMessage().getText());

            if (util.checkCreateTodoFormat(msg)) {
                TodoTaskModel todoTaskModel = splitTodoTask(msg);
                todoTaskModel.setLineId(String.valueOf(event.getSource().getUserId()));
                saveTodoTask(todoTaskModel);
                getTodoTaskByLineId(String.valueOf(event.getSource().getUserId()));

                return new TextMessage("Todo List: \n"
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
}
