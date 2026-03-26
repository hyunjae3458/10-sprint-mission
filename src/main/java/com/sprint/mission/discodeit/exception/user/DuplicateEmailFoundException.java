package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public class DuplicateEmailFoundException extends UserException {
    public DuplicateEmailFoundException(String email) {
        super(ErrorCode.DUPLICATE_EMAIL, Map.of("email", email));
    }
}
