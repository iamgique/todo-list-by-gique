package com.gique.todo.services;

import com.gique.todo.constants.Constants;
import com.gique.todo.models.TodoTaskModel;
import com.gique.todo.models.TodoModel;
import com.gique.todo.utils.Util;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    private static final Logger log = LogManager.getLogger(MessageService.class);

    @NonNull
    private TodoListService todoListService;

    @NonNull
    private final Util util;

    @Autowired
    public MessageService(TodoListService todoListService, Util util){
        this.todoListService = todoListService;
        this.util = util;
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
                todoListService.saveTodoTask(todoTaskModel);
                List<TodoModel> todoModelList = todoListService.getTodoTaskByLineId(String.valueOf(event.getSource().getUserId()));

                Optional.ofNullable(todoModelList).orElseThrow(() -> new Exception());

                StringBuffer resp = new StringBuffer();
                resp.append("Todo List: \n");
                for(TodoModel todoModel : todoModelList){
                    resp.append("Task: " + todoModel.getTask());
                    resp.append("\n Status: " + todoModel.getStatus());
                    resp.append(" Due date: " + todoModel.getDueDate() + "\n");
                }

                return new TextMessage(resp.toString());
            } else if (msg.equals("edit")) {
                return new TextMessage("You can edit todo list in link below: \n https://bit.ly/2rU3RCy");
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
