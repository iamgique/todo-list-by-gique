package com.gique.todo.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class StatusModel implements Serializable {
    //@ApiModelProperty(value = "code 0 => success other => error somthing" ,example = "0")
    @JsonProperty("code")
    private String code;
    //@ApiModelProperty(value = "message description information" ,example = "success")
    @JsonProperty("message")
    private String message;
    public StatusModel(String code, String message) {
        this.code = code;
        this.message = message;
    }
    public StatusModel() {
    }
}