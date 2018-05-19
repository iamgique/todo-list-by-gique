package com.gique.todo.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TodoModel {
    private int id;
    private String lineId = "";
    private String task = "";
    private String status = "";
    private String important = "";
    private String dueDate = "";
    private String createdAt = "";
    private String updatedAt = "";
}
