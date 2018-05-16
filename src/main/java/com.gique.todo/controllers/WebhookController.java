package com.gique.todo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("webhook")
public class WebhookController {

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus( HttpStatus.OK )
    public ResponseEntity<String> webhook(@RequestHeader(value = "X-Line-Signature", required = false) String signature {
        return new ResponseEntity<>("Test" + String.valueOf(signature), HttpStatus.OK);
    }
}
