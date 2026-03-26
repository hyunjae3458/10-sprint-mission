package com.sprint.mission.discodeit.exception.readStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class ReadStatusNotFoundException extends RuntimeException {
    public ReadStatusNotFoundException(UUID readStatusId) {
        super(ErrorCode.READSTATUS_NOT_FOUND, Map.of("readStatusId", readStatusId));
    }
}
