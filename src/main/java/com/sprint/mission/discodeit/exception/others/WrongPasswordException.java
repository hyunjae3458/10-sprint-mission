package com.sprint.mission.discodeit.exception.others;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import lombok.Getter;

import java.util.Map;

@Getter
public class WrongPasswordException extends DiscodeitException {
    public WrongPasswordException() {
        super(ErrorCode.WRONG_PASSWORD, Map.of());
    }
}
