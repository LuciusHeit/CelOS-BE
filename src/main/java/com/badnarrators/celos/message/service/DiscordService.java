package com.badnarrators.celos.message.service;

import com.badnarrators.celos.message.model.DiscordMessage;
import com.badnarrators.celos.message.model.MessageAttributes;
import com.badnarrators.celos.message.repository.MessageRepository;
import com.badnarrators.celos.user.entity.User;
import com.badnarrators.celos.user.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class DiscordService {

    @Value("${spring.application.name}")
    private String appName;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserService userService;

    @Value("${discord.webhooks.notifications.url}")
    private String notificationsUrl;

    public boolean messageNotification(MessageAttributes messageAttributes) {
        User sender = userService.getById(messageAttributes.getSenderId());
        String content = "Message to: " + messageAttributes.getReceiver() + "\n" + messageAttributes.getContent();
        String senderName = sender.getUsername() + "@" + sender.getDomain();

        return send(content, senderName);
    }

    public boolean send(String content){
        return this.send(content, appName);
    }

    public boolean send(String content, String username) {RestTemplate restTemplate = new RestTemplate();
        String uri = notificationsUrl;

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

        DiscordMessage discordMessage = new DiscordMessage(content, username);
        HttpEntity<DiscordMessage> request = new HttpEntity<>(discordMessage, headers);

        try {
            restTemplate.postForEntity(uri, request, String.class);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @PostConstruct
    public void init() {
        this.send("CelOS service has been started");
    }
}
