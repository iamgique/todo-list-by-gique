package com.gique.todo.constants;

import lombok.Getter;

@Getter
public enum Response {
    SUCCESS("success"),
    ERROR("error"),
    FAIL("fail"),
    INVALID("invalid"),
    SUCCESS_CODE("0"),
    FAIL_CODE("1");

    private final String content;

    Response(String content) {
        this.content = content;
    }
}
