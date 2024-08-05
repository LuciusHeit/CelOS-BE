package com.badnarrators.celos.user.controller;


import com.badnarrators.celos.user.model.LoginAttributes;
import com.badnarrators.celos.user.model.SimpleUser;
import com.badnarrators.celos.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public SimpleUser login(@RequestBody LoginAttributes login) {
        return userService.login(login.username, login.password);
    }


    @PostMapping("/login/id")
    public SimpleUser loginById(@RequestBody LoginAttributes login) {
        return userService.loginById(Long.parseLong(login.username), login.password);
    }


}
