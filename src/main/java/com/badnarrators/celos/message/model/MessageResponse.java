package com.badnarrators.celos.message.model;

import com.badnarrators.celos.message.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MessageResponse {

    private String id;

    private String content;

    private String receiver;

    private String sender;

    private boolean read;

    private LocalDateTime sentTime;

    public MessageResponse(Message message) {
        this.id = message.getId().toString();
        this.content = message.getMessage();
        this.receiver = message.getReceiver().getUsername()+"@"+message.getReceiver().getDomain();
        this.sender = message.getSender().getUsername()+"@"+message.getSender().getDomain();
        this.read = message.isRead();
        this.sentTime = message.getSentTime();
    }

}
