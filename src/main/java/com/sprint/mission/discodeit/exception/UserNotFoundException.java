package com.sprint.mission.discodeit.exception;

import java.util.UUID;

public class UserNotFoundException extends  RuntimeException{
    public UserNotFoundException(String username) {
        super("User with username " + username + " not found");
    }

    public UserNotFoundException(UUID userId) {
        super("User with id " + userId + " not found"); // "Author with id..." 로 바꿔도 됨
    }
}
