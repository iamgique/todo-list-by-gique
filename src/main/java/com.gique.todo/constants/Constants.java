package com.gique.todo.constants;

import lombok.Getter;

@Getter
public enum Constants {
    CANNOT_POST_TODO_LIST("Cannot post Todo list task because something went wrong."),
    CANNOT_PUSH_MSG_TO_USER("System cannot send message to users"),
    EXAMPLE_POST_TODO_LIST("For example make todo list \n task : date/month/year : time e.g Buy milk : 3/5/18 : 13:00 \n task : today : time e.g Finsh writing shopping list : today : 15:30 \n task : tomorrow : time e.g Watch movie : tomorrow : 18:00 \n ** If not specific time then use default time as 12:00 pm ** \n ** todo list by input word edit in chat and system will return webview **");

    private final String content;
    Constants(String content) {
        this.content = content;
    }

}
