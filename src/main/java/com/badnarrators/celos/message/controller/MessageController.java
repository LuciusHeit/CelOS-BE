package com.badnarrators.celos.message.controller;


import com.badnarrators.celos.message.entity.Message;
import com.badnarrators.celos.message.model.MessageAttributes;
import com.badnarrators.celos.message.model.MessageIdList;
import com.badnarrators.celos.message.model.MessageResponse;
import com.badnarrators.celos.message.service.MessageService;
import com.badnarrators.celos.user.model.LoginAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/v1/msg")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    public boolean send(@RequestBody MessageAttributes messageAttributes) {
        return messageService.send(messageAttributes);
    }

    @PostMapping(value = {"/read", "/seen"})
    public boolean setRead(@RequestBody MessageIdList messageIdList) {
        return messageService.setRead(messageIdList);
    }

    @GetMapping("/unread/{receiverId}")
    public int getUnreadCount(@PathVariable String receiverId) {
        return messageService.getUnreadCount(receiverId);
    }

    @GetMapping("/sent")
    public List<Message> getSent(String senderId) {
        return messageService.getSent(senderId);
    }

    @PostMapping("/received")
    public List<MessageResponse> getReceived(@RequestBody LoginAttributes login) {
        return messageService.getReceived(login);
    }
}
