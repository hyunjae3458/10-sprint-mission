package com.sprint.mission.discodeit.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class WrongPasswordException extends DiscodeitException{
    public WrongPasswordException() {
        super(ErrorCode.WRONG_PASSWORD, Map.of());
    }
}
