package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.entity.User;
import lombok.Getter;

import java.util.UUID;

@Getter
public class UserNotFoundException extends  RuntimeException{
    private final String username;
    private final UUID userId;

    public UserNotFoundException(String username) {
        super("User with username " + username + " not found");
        this.username = username;
        this.userId = null;
    }

    public UserNotFoundException(UUID userId) {
        super("User with id " + userId + " not found");
        this.username = null;
        this.userId = userId;
    }
}
