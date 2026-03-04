package com.sprint.mission.discodeit.dto.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class MessageDto {
    private UUID id;
    private UUID authorId;
    private UUID channelId;
    private Instant createdAt;
    private Instant updatedAt;
    private List<UUID> attachmentIds;
    private String content;

}
