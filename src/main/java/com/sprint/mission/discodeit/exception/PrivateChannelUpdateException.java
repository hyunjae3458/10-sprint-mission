package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.entity.type.ChannelType;
import lombok.Getter;

import java.util.UUID;

@Getter
public class PrivateChannelUpdateException extends RuntimeException {
    private final UUID channelId;
    public PrivateChannelUpdateException(UUID channelId) {
        super("can't update channel with channelId: " + channelId);
        this.channelId = channelId;
    }
}
