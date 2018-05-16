package com.gique.todo.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseModel implements Serializable {
    @JsonProperty("status")
    private StatusModel status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private Object data;

    public ResponseModel(StatusModel message) {
        this.status = message;
    }
    public ResponseModel(StatusModel message, Object data) {
        this.status = message;
        this.data = data;
    }
    public ResponseModel(Object data) {
        this.status = null;
        this.data = data;
    }
    public ResponseModel(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public ResponseModel() {
    }

    public HttpEntity<ResponseModel> build(HttpStatus status) {
        return new ResponseEntity<>(new ResponseModel(this.status, this.data), status);
    }
}
