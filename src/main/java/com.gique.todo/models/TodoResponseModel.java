package com.gique.todo.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TodoResponseModel {
    private String id = "";
    private String lineId = "";
    private String task = "";
    private String status = "";
    private String important = "";
    private String dueDate = "";
    private String createdAt = "";
    private String updatedAt = "";
}
