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

    public List<String> listLineId() throws SQLException {
        log.info("listLineId");
        List<String> resp = new ArrayList<>();
        Statement stmt = dataSource.getConnection().createStatement();
        try {
            ResultSet rs = stmt.executeQuery("SELECT line_id FROM todo GROUP BY line_id");
            if(rs != null){
                while (rs.next()) {
                    resp.add(rs.getString("line_id"));
                }
            }
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
        } finally {
            if(stmt != null){ stmt.close(); }
            return resp;
        }
    }

    public String listCountStatusByLineId(String lineId, String status) throws SQLException {
        log.info("listCountStatusByLineId");
        String resp = null;
        Statement stmt = dataSource.getConnection().createStatement();
        try {
            ResultSet rs = stmt.executeQuery("SELECT count(status) AS count FROM todo WHERE line_id = '"+lineId+"' AND status = '"+status+"';");
            if(rs != null){
                while (rs.next()) {
                    resp = rs.getString("count");
                }
            } else {
                resp = "0";
            }
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
        } finally {
            if(stmt != null){ stmt.close(); }
            return resp;
        }
    }

    public void saveTodoTask(TodoTaskModel todoTaskModel) throws SQLException, ParseException {
        log.info("saveTodoTask");
        String dueDate = util.getDueDate(todoTaskModel.getDate(), todoTaskModel.getTime());
        Statement stmt = dataSource.getConnection().createStatement();
        try {
            stmt.executeUpdate("INSERT INTO todo (line_id, task, status, important, due_date, created_at, updated_at) " +
                    "VALUES ('" + todoTaskModel.getLineId() + "', '" + todoTaskModel.getTask() + "', 'incomplete', '0', '" + dueDate + "', now(), now());");
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
        } finally {
            if(stmt != null){ stmt.close(); }
        }
    }

    public void updateTodoTask(TodoModel todoModel) throws SQLException {
        log.info("updateTodoTask");
        Statement stmt = dataSource.getConnection().createStatement();
        try {
            stmt.executeUpdate("UPDATE todo SET task = '" + todoModel.getTask() + "', " +
                    "status = '" + todoModel.getStatus() + "', " +
                    "important = '" + todoModel.getImportant() + "', due_date = '" + Util.convertFormatDateFromdMyytoyyyyMMdd(todoModel.getDueDate()) + "', " +
                    "updated_at = now() WHERE id = " + todoModel.getId() + ";");
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
        } finally {
            if(stmt != null){ stmt.close(); }
        }
    }

    public List<TodoModel> getTodoTaskByLineId(String lineId) throws SQLException, ParseException {
        log.info("getTodoTaskByLineId: {}", lineId);
        List<TodoModel> todoResponseModelList = new ArrayList<>();
        TodoModel todoResponseModel;
        Statement stmt = dataSource.getConnection().createStatement();

        try {
            ResultSet rs = stmt.executeQuery("SELECT id, line_id, task, status, important, due_date, created_at, updated_at FROM todo " +
                    "WHERE line_id = '"+lineId+"' ORDER BY important DESC, due_date ASC");

            if(rs != null){
                while (rs.next()) {
                    todoResponseModel = new TodoModel();
                    todoResponseModel.setId(rs.getInt("id"));
                    todoResponseModel.setLineId(rs.getString("line_id"));
                    todoResponseModel.setTask(rs.getString("task"));
                    todoResponseModel.setStatus(rs.getString("status").trim());
                    todoResponseModel.setImportant(rs.getString("important"));
                    todoResponseModel.setDueDate(Util.convertFormatDateFromyyyyMMddtodMyy(rs.getString("due_date")));
                    todoResponseModel.setCreatedAt(Util.convertFormatDateFromyyyyMMddtodMyy(rs.getString("created_at")));
                    todoResponseModel.setUpdatedAt(Util.convertFormatDateFromyyyyMMddtodMyy(rs.getString("updated_at")));
                    todoResponseModelList.add(todoResponseModel);
                }
            }
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
        } finally {
            if(stmt != null){ stmt.close(); }
        }

        return todoResponseModelList;
    }

}
