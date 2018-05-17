package com.gique.todo.services;

import com.gique.todo.constants.Constants;
import com.gique.todo.models.TodoTaskModel;
import com.gique.todo.models.TodoResponseModel;
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
import java.util.ArrayList;
import java.util.List;
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

    public List<TodoResponseModel> getTodoTaskByLineId(String lineId) throws SQLException {
        List<TodoResponseModel> todoResponseModelList = new ArrayList<>();
        TodoResponseModel todoResponseModel;

        Statement stmt = dataSource.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("SELECT id, line_id, task, status, important, due_date, created_at, updated_at FROM todo " +
                "WHERE line_id = '"+lineId+"' ORDER BY important, due_date ASC");

        if(rs != null){
            while (rs.next()) {
                todoResponseModel = new TodoResponseModel();
                todoResponseModel.setId(rs.getString("id"));
                todoResponseModel.setLineId(rs.getString("line_id"));
                todoResponseModel.setTask(rs.getString("task"));
                todoResponseModel.setStatus(rs.getString("status"));
                todoResponseModel.setImportant(rs.getString("important"));
                todoResponseModel.setDueDate(rs.getString("due_date"));
                todoResponseModel.setCreatedAt(rs.getString("created_at"));
                todoResponseModel.setUpdatedAt(rs.getString("updated_at"));
                todoResponseModelList.add(todoResponseModel);
            }
        }
        return todoResponseModelList;
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
                List<TodoResponseModel> todoResponseModelList = getTodoTaskByLineId(String.valueOf(event.getSource().getUserId()));

                Optional.ofNullable(todoResponseModelList).orElseThrow(() -> new Exception());

                StringBuffer resp = new StringBuffer();
                resp.append("Todo List: \n");
                for(TodoResponseModel todoResponseModel : todoResponseModelList){
                    resp.append("Task: " + todoResponseModel.getTask());
                    resp.append("\n Status: " + todoResponseModel.getStatus());
                    resp.append(" Due date: " + todoResponseModel.getDueDate() + "\n");
                }

                return new TextMessage(resp.toString());
            } else if (msg.equals("edit")) {
                return new TextMessage("Edit todo list");
            } else {
                return new TextMessage(Constants.EXAMPLE_POST_TODO_LIST.getContent());
            }

        } catch (Exception e) {
            return new TextMessage(Constants.CANNOT_POST_TODO_LIST.getContent());
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
