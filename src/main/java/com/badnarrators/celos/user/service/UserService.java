package com.badnarrators.celos.user.service;

import com.badnarrators.celos.user.entity.User;
import com.badnarrators.celos.user.model.SimpleUser;
import com.badnarrators.celos.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public SimpleUser get(String id) {
        long i = 0L;
        try {
            i = Long.parseLong(id);
        } catch (NumberFormatException e) {
            return null;
        }
        Optional<User> user = userRepository.findById(i);

        return user.map(SimpleUser::new).orElse(null);
    }

    public SimpleUser getSimpleByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);

        return user.map(SimpleUser::new).orElse(null);
    }
    public User getByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);

        return user.orElse(null);
    }
    public User getById(Long id) {
        Optional<User> user = userRepository.findById(id);

        return user.orElse(null);
    }
    public SimpleUser login(String username, String password) {
        Optional<User> user = null;
        String userDomain = "";
        String[] userArray = username.split("@");
        if (userArray.length > 1) {
            LOGGER.info("User has @: " + username);
            if (userArray.length != 2) {
                LOGGER.info("Invalid username: " + username);
                return null;
            }
            else {
                LOGGER.info("User" + userArray[0] + " has domain: " + userArray[1]);
                username = userArray[0];
                userDomain = userArray[1];
            }
        }


        if (username == null || password == null) {
            LOGGER.info("Invalid username or password: " + username + " " + password);
            return null;
        }
        if (!userRepository.existsByUsernameIgnoreCase(username)) {
            LOGGER.info("User does not exist: " + username);
            return null;
        }

        if(!userDomain.equals("")) {
            if (!userRepository.existsByUsernameIgnoreCaseAndDomainIgnoreCase(username, userDomain)) {
                LOGGER.info("User with domain does not exist: " + username);
                return null;
            }
            user = Optional.ofNullable(userRepository.findByUsernameIgnoreCaseAndDomainIgnoreCase(username, userDomain));

            return new SimpleUser(user.get(), true);
        }
        else {
            if (userRepository.countByUsernameIgnoreCase(username) > 1) {
                LOGGER.info("User exists multiple times: " + username);
                List<User> users = userRepository.findAllByUsernameIgnoreCase(username);
                user = users.stream().filter(u -> u.getDomain().equals("celos.com")).findFirst();
                return user.map(value -> new SimpleUser(value, true)).orElse(null);
            }

            if (!userRepository.existsByUsername(username)) {
                LOGGER.info("User does not exist: " + username);
                return null;
            }

            LOGGER.info("User logging in: " + username);
            user = userRepository.findByUsernameAndPassword(username, password);
            return user.map(SimpleUser::new).orElse(null);
        }


    }

    public SimpleUser loginById(Long id, String password) {
        Optional<User> user = null;

        if (id == null || password == null) {
            LOGGER.info("Invalid id or password: " + id + " " + password);
            return null;
        }
        if (!userRepository.existsById(id)) {
            LOGGER.info("User id does not exist: " + id);
            return null;
        }

        LOGGER.info("User id logging in: " + id);
        user = userRepository.findByIdAndPassword(id, password);

        if(userRepository.countByUsernameIgnoreCase(user.get().getUsername()) > 1) {
            LOGGER.info("Username exists multiple times: " + user.get().getUsername());
            return new SimpleUser(user.get(), true);
        }

        return user.map(SimpleUser::new).orElse(null);

    }

}
