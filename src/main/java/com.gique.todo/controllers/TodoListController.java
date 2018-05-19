package com.gique.todo.controllers;

import com.gique.todo.Application;
import com.gique.todo.constants.Response;
import com.gique.todo.models.ResponseModel;
import com.gique.todo.models.StatusModel;
import com.gique.todo.models.TodoModel;
import com.gique.todo.services.TodoListService;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("todolist/v1")
public class TodoListController {
    private static final Logger log = LogManager.getLogger(TodoListController.class);

    @NonNull
    private final TodoListService todoListService;

    @Autowired
    public TodoListController(TodoListService todoListService){
        this.todoListService = todoListService;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public HttpEntity<ResponseModel> list(@RequestParam(value = "line_id", required = false, defaultValue = "") String lineId,
                                                  HttpServletRequest request , HttpServletResponse response) throws Exception {
        log.info("get list");
        HttpEntity<ResponseModel> responseModel = null;
        try {
            List<TodoModel> todoModels = todoListService.getTodoTaskByLineId(lineId);
            StatusModel status = new StatusModel(Response.SUCCESS_CODE.getContent(),Response.SUCCESS.getContent());
            responseModel = new HttpEntity<>(new ResponseModel(status, todoModels));
        } catch (Exception e) {
            log.error(e.getMessage());
            responseModel = new HttpEntity<>(new ResponseModel(new StatusModel(Response.FAIL_CODE.getContent(), Response.ERROR.getContent()), ""));
        }
        return responseModel;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<ResponseModel> edit(@RequestBody TodoModel todoModel) throws Exception {
        log.info("edit todo list" );
        HttpEntity<ResponseModel> responseModel = null;
        try {
            Optional.ofNullable(todoModel.getId()).orElseThrow(() -> new Exception());
            log.info("id: {} line_id: {}", todoModel.getId(), todoModel.getLineId());
            todoListService.updateTodoTask(todoModel);

            List<TodoModel> todoModels = todoListService.getTodoTaskByLineId(todoModel.getLineId());
            StatusModel status = new StatusModel(Response.SUCCESS_CODE.getContent(),Response.SUCCESS.getContent());
            responseModel = new HttpEntity<>(new ResponseModel(status, todoModels));
        } catch (Exception e) {
            log.error(e.getMessage());
            responseModel = new HttpEntity<>(new ResponseModel(new StatusModel(Response.FAIL_CODE.getContent(), Response.ERROR.getContent()), ""));
        }
        return responseModel;
    }


}
