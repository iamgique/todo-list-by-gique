package com.gique.todo.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageModel {
    @JsonProperty("type")
    private String type;

    @JsonProperty("text")
    private String text;
}
