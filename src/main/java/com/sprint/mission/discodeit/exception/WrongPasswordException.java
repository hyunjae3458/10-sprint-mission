package com.sprint.mission.discodeit.exception;

import lombok.Getter;

@Getter
public class WrongPasswordException extends RuntimeException{
    public final ErrorCode errorCode;
    public WrongPasswordException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode =errorCode;
    }
}
