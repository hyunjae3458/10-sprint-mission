package com.sprint.mission.discodeit.exception;

import lombok.Getter;

@Getter
public class DuplicateEmailFoundException extends RuntimeException {
    private final String email;

    public DuplicateEmailFoundException(String email) {
        super("email: " + email + " is already exist");
        this.email = email;
    }
}
