package com.gique.todo.controllers;

import com.gique.todo.constants.Response;
import com.gique.todo.models.ResponseModel;
import com.gique.todo.models.StatusModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class IndexController {

    public IndexController() {

    }

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public HttpEntity<ResponseModel> getTodoList() throws Exception {
        HttpEntity<ResponseModel> responseModel = null;

        StatusModel status = new StatusModel(Response.SUCCESS_CODE.getContent(), Response.SUCCESS.getContent());
        responseModel = new HttpEntity<>(new ResponseModel(status, "INDEX"));

        return responseModel;
    }
}
