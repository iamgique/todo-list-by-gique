package com.gique.todo.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PushMsgModel {

    @JsonProperty("to")
    private List<String> to;

    @JsonProperty("messages")
    private List<MessageModel> messages;

    public PushMsgModel(){

    }
}
