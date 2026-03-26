package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.entity.type.ChannelType;
import lombok.Getter;

import java.util.UUID;

@Getter
public class PrivateChannelUpdateException extends RuntimeException {
    private final UUID channelId;
    private final ErrorCode errorCode;
    public PrivateChannelUpdateException(UUID channelId, ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.channelId = channelId;
    }
}
