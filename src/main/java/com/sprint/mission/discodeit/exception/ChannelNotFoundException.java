package com.sprint.mission.discodeit.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ChannelNotFoundException extends RuntimeException{
    private final UUID channelId;
    public ChannelNotFoundException(UUID channelId) {
        super("Channel with id " + channelId + " not found");
        this.channelId = channelId;
    }
}
