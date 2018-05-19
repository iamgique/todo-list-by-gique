package com.gique.todo.services;

import com.gique.todo.models.MessageModel;
import com.gique.todo.models.PushMsgModel;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExternalService {
    private static final Logger log = LogManager.getLogger(ExternalService.class);

    @Value("${line.server.path.push.message}")
    private String pushMsgUri;

    @Value("${line.bot.channel-token}")
    private String accessToken;

    @Autowired
    public ExternalService (){
    }

    public void pushMsg(String lineId, List<MessageModel> msg){
        log.info("pushMsg to: {}", lineId);

        List<String> lineIds = new ArrayList<>();
        lineIds.add(lineId);

        PushMsgModel pushMsgModel = new PushMsgModel();
        pushMsgModel.setTo(lineIds);
        pushMsgModel.setMessages(msg);

        String push = new Gson().toJson(pushMsgModel);

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            headers.set("Authorization", "Bearer " + accessToken);
            HttpEntity<String> request = new HttpEntity<String>(push, headers);

            URI targetUrl = UriComponentsBuilder.fromUriString(pushMsgUri).build().toUri();
            log.info(targetUrl);
            log.info(request.getHeaders());
            log.info(request.getBody());
            ResponseEntity<String> response = restTemplate.postForEntity( targetUrl, request , String.class );
        } catch (Exception e) {
            log.error("Exception Message: {}", e.getMessage());
            log.error("Exception: {}", e);
        }
    }


}
