package com.gique.todo.controllers;

import com.gique.todo.Application;
import com.gique.todo.constants.Response;
import com.gique.todo.models.ResponseModel;
import com.gique.todo.models.StatusModel;
import com.gique.todo.models.TodoResponseModel;
import com.gique.todo.services.TodoListService;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("todolist/v1")
public class TodoListController {
    private static final Logger log = LogManager.getLogger(Application.class);

    @NonNull
    private final TodoListService todoListService;

    @Autowired
    public TodoListController(TodoListService todoListService){
        this.todoListService = todoListService;
    }

    @RequestMapping(value = "/getlist", method = RequestMethod.GET)
    public HttpEntity<ResponseModel> getTempToken(@RequestParam(value = "line_id", required = false, defaultValue = "") String lineId,
                                                  HttpServletRequest request , HttpServletResponse response) throws Exception {
        log.info("getlist");
        HttpEntity<ResponseModel> responseModel = null;
        try {
            List<TodoResponseModel> todoResponseModelList = todoListService.getTodoTaskByLineId(lineId);
            StatusModel status = new StatusModel(Response.SUCCESS_CODE.getContent(),Response.SUCCESS.getContent());
            responseModel = new HttpEntity<>(new ResponseModel(status, todoResponseModelList));
        } catch (Exception e) {
            responseModel = new HttpEntity<>(new ResponseModel(new StatusModel(Response.FAIL_CODE.getContent(), Response.ERROR.getContent()), ""));
        }
        return responseModel;
    }


}
