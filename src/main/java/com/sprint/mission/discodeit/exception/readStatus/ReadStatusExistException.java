package com.sprint.mission.discodeit.exception.readStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public class ReadStatusExistException extends ReadStatusException {
    public ReadStatusExistException() {
        super(ErrorCode.READSTATUS_EXIST, Map.of());
    }
}
