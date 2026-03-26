package com.sprint.mission.discodeit.exception.userStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class UserStatusNotFoundException extends UserStatusException {
    public UserStatusNotFoundException(UUID userId) {
        super(ErrorCode.USERSTATUS_NOT_FOUND, Map.of("userId",userId));
    }
}
