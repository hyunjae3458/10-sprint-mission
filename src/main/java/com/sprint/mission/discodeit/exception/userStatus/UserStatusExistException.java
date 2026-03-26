package com.sprint.mission.discodeit.exception.userStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class UserStatusExistException extends UserStatusException{

    public UserStatusExistException(UUID userId) {
        super(ErrorCode.USERSTATUS_EXIST, Map.of("userId", userId));
    }
}
