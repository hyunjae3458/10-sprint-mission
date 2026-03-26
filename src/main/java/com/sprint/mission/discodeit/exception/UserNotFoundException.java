package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.entity.User;
import lombok.Getter;

import java.util.UUID;

@Getter
public class UserNotFoundException extends  RuntimeException{
    private final String username;
    private final UUID userId;
    private final ErrorCode errorCode;

    public UserNotFoundException(String username, ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.username = username;
        this.userId = null;
        this.errorCode = errorCode;
    }

    public UserNotFoundException(UUID userId, ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.username = null;
        this.userId = userId;
        this.errorCode = errorCode;
    }
}
