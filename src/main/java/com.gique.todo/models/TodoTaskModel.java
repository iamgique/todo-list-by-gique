package com.gique.todo.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TodoTaskModel {
    private String task = "";
    private String date = "";
    private String time = "";
}
