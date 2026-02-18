package com.sprint.mission.discodeit.dto.readStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ReadStatusDto {
    private UUID id;
    private UUID userId;
    private Instant createdAt;
    private Instant updatedAt;
    private UUID channelId;
    private Instant lastReadAt;
}
