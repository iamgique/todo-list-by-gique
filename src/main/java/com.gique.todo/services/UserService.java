package com.gique.todo.services;

import com.linecorp.bot.client.LineMessagingClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private LineMessagingClient lineMessagingClient;

    @Autowired
    public UserService(LineMessagingClient lineMessagingClient){
        this.lineMessagingClient = lineMessagingClient;
    }

    public String getUserProfile(String userId){
        final String[] resp = {""};
        if (userId != null) {
            lineMessagingClient
                    .getProfile(userId)
                    .whenComplete((profile, throwable) -> {
                        if (throwable != null) {
                            resp[0] = throwable.getMessage().toString();
                            return;
                        }
                        resp[0] = "Display name: " + profile.getDisplayName() + " Status message: " + profile.getStatusMessage() + "\n";
                    });
        }
        return resp[0];
    }
}
