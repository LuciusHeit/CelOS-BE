package com.badnarrators.celos.message.service;

import com.badnarrators.celos.message.entity.Message;
import com.badnarrators.celos.message.model.MessageAttributes;
import com.badnarrators.celos.message.model.MessageIdList;
import com.badnarrators.celos.message.model.MessageResponse;
import com.badnarrators.celos.message.repository.MessageRepository;
import com.badnarrators.celos.user.entity.User;
import com.badnarrators.celos.user.model.LoginAttributes;
import com.badnarrators.celos.user.repository.UserRepository;
import com.badnarrators.celos.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private DiscordService discordService;

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);


    public boolean send(MessageAttributes messageAttributes) {
        LOGGER.info("User " + messageAttributes.getSenderId() + " sending message: " + messageAttributes.toString() + " to: " + messageAttributes.getReceiver());

        Long senderId = messageAttributes.getSenderId();
        String receiver = messageAttributes.getReceiver();
        String message = messageAttributes.getContent();
        String password = messageAttributes.getPassword();
        String receiverDomain = "";
        User receiverUser = null;
        User senderUser = null;

        if (userService.loginById(senderId, password) == null) {
            LOGGER.info("Login failed: " + senderId);
            return false;
        }

        if (senderId == null || receiver == null || message == null) {
            LOGGER.info("Invalid message: " + messageAttributes.toString());
            return false;
        }

        String receiverArray[] = receiver.split("@");
        if (receiverArray.length != 1) {
            if (receiverArray.length != 2) {
                LOGGER.info("Invalid username: " + senderId + " or " + receiver);
                return false;
            }
            else {
                receiver = receiverArray[0];
                receiverDomain = receiverArray[1];
            }
        }

        if (!Objects.equals(receiverDomain, "")){
            if (!userRepository.existsById(senderId) || !userRepository.existsByUsernameIgnoreCaseAndDomainIgnoreCase(receiver, receiverDomain)) {
                LOGGER.info("User does not exist: " + senderId + " or " + receiver + "@" + receiverDomain);
                return false;
            }

            receiverUser = userRepository.findByUsernameIgnoreCaseAndDomainIgnoreCase(receiver, receiverDomain);
        }
        else {
            if (!userRepository.existsById(senderId) || !userRepository.existsByUsernameIgnoreCase(receiver)) {
                LOGGER.info("User does not exist: " + senderId + " or " + receiver);
                return false;
            }
            if(userRepository.countByUsernameIgnoreCase(receiver) > 1) {
                LOGGER.info("User exists multiple times: " + receiver);
                List<User> users = userRepository.findAllByUsernameIgnoreCase(receiver);
                receiverUser = users.stream().filter(u -> u.getDomain().equals("celos.com")).findFirst().orElse(null);
            }
            else{
                receiverUser = userRepository.findByUsernameIgnoreCase(receiver).orElse(null);
            }
            LOGGER.info("Receiver user: " + receiverUser.getUsername());
            senderUser = userRepository.findById(senderId).orElse(null);
            if (receiverUser == null || senderUser == null) {
                LOGGER.info("User does not exist: " + senderId + " or " + receiver + " (last check)");
                return false;
            }

        }
        Message newMessage = new Message();
        newMessage.setMessage(message);
        newMessage.setSender(senderUser);
        newMessage.setReceiver(receiverUser);
        messageRepository.save(newMessage);

        discordService.messageNotification(messageAttributes);

        return true;
    }


    public int getUnreadCount(String receiverId) {
        long receiverIdLong = Long.parseLong(receiverId);
        if (!userRepository.existsById(receiverIdLong)) {
            return -1;
        }
        User receiver = userRepository.findById(receiverIdLong).get();
        return messageRepository.countByReceiverAndRead(receiver, false);
    }

    public boolean setRead(MessageIdList messageIdList) {
        List<UUID> ids = messageIdList.getIds();
        Long userId = messageIdList.getUserId();
        String password = messageIdList.getPassword();
        if (userService.loginById(userId, password) == null) {
            return false;
        }
        User user = userService.getById(userId);
        for (UUID id : ids) {
            Message message = messageRepository.findById(id).orElse(null);
            if (message == null) {
                continue;
            }
            if (message.getReceiver().equals(user)) {
                message.setRead(true);
                messageRepository.save(message);
            }
        }
        return true;
    }

    public List<Message> getSent(String senderId) {
        LOGGER.info("Get sent messages: " + senderId);
        long senderIdLong = Long.parseLong(senderId);
        if (!userRepository.existsById(senderIdLong)) {
            return null;
        }
        User sender = userRepository.findById(senderIdLong).get();
        return messageRepository.findBySender(sender);
    }

    public List<MessageResponse> getReceived(LoginAttributes login) {
        LOGGER.info("Get received messages: " + login.id);
        if (userService.loginById(login.id, login.password) == null) {
            return null;
        }
        User receiver = userService.getById(login.id);
        List<Message> messages = messageRepository.findByReceiver(receiver);

        return messages.stream().map(MessageResponse::new).toList();
    }

}
