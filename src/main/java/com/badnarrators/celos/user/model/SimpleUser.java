package com.badnarrators.celos.user.model;

import com.badnarrators.celos.user.entity.User;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleUser {
    private String id;
    private String username;
    private String password;
    private boolean player;
    private boolean admin;

    public SimpleUser(User user) {
        this.id = user.getId().toString();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.admin = user.isAdmin();
        this.player = user.isPlayer();
    }
    public SimpleUser(User user, boolean sendWithDomain) {
        if (sendWithDomain) {
            this.username = user.getUsername().toLowerCase() + "@" + user.getDomain();
        } else {
            this.username = user.getUsername();
        }
        this.id = user.getId().toString();
        this.password = user.getPassword();
        this.admin = user.isAdmin();
        this.player = user.isPlayer();
    }

}
