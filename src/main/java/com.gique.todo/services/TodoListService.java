package com.gique.todo.services;

import com.gique.todo.models.TodoModel;
import com.gique.todo.models.TodoTaskModel;
import com.gique.todo.utils.Util;
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

@Service
public class TodoListService {
    private static final Logger log = LogManager.getLogger(MessageService.class);

    @Autowired
    private DataSource dataSource;

    @NonNull
    private final Util util;

    public TodoListService(Util util){
        this.util = util;
    }

    public void saveTodoTask(TodoTaskModel todoTaskModel) throws SQLException, ParseException {
        log.info("saveTodoTask");
        String dueDate = util.getDueDate(todoTaskModel.getDate(), todoTaskModel.getTime());
        Statement stmt = dataSource.getConnection().createStatement();
        stmt.executeUpdate("INSERT INTO todo (line_id, task, status, important, due_date, created_at, updated_at) " +
                "VALUES ('"+todoTaskModel.getLineId()+"', '"+todoTaskModel.getTask()+"', 'incomplete', '0', '"+dueDate+"', now(), now());");
    }

    public void updateTodoTask(TodoModel todoModel) throws SQLException {
        log.info("updateTodoTask");
        Statement stmt = dataSource.getConnection().createStatement();
        stmt.executeUpdate("UPDATE todo SET line_id = '"+todoModel.getLineId()+"', " +
                "task = '"+todoModel.getTask()+"', status = '"+todoModel.getStatus()+"', " +
                "important = '"+todoModel.getImportant()+"', due_date = '"+todoModel.getDueDate()+"', " +
                "updated_at = now() WHERE id = '"+todoModel.getId()+"';");
    }

    public List<TodoModel> getTodoTaskByLineId(String lineId) throws SQLException {
        log.info("getTodoTaskByLineId: {}", lineId);
        List<TodoModel> todoResponseModelList = new ArrayList<>();
        TodoModel todoResponseModel;

        Statement stmt = dataSource.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("SELECT id, line_id, task, status, important, due_date, created_at, updated_at FROM todo " +
                "WHERE line_id = '"+lineId+"' ORDER BY important DESC, due_date ASC");

        if(rs != null){
            while (rs.next()) {
                todoResponseModel = new TodoModel();
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

}
