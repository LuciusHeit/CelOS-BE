package com.badnarrators.celos.message.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MessageAttributes {

    Long senderId;
    String password;
    String receiver;
    String content;

}
