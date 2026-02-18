package com.sprint.mission.discodeit.exception;

import java.util.UUID;

public class ChannelNotFoundException extends RuntimeException{
    public ChannelNotFoundException(UUID channelId) {
        super("Channel with id " + channelId + " not found");
    }
}
