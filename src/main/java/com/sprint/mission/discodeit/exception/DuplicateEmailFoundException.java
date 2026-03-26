package com.sprint.mission.discodeit.exception;

import lombok.Getter;

@Getter
public class DuplicateEmailFoundException extends RuntimeException {
    private final String email;
    private final ErrorCode errorCode;
    public DuplicateEmailFoundException(String email, ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.email = email;
        this.errorCode = errorCode;
    }
}
