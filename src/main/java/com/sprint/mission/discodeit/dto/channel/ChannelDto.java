package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.type.ChannelType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ChannelDto {
    private UUID id;
    private String name;
    private String description;
    private List<UUID> participantIds;
    private Instant lastMessageAt;
    private ChannelType type;
}
