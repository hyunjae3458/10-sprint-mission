package com.sprint.mission.discodeit.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ChannelNotFoundException extends RuntimeException{
    private final UUID channelId;
    private final ErrorCode errorCode;
    public ChannelNotFoundException(UUID channelId,ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.channelId = channelId;
        this.errorCode = errorCode;
    }
}
