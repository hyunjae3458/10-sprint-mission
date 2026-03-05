package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class MessageDto {
    private UUID id;
    private UserDto author;
    private UUID channelId;
    private Instant createdAt;
    private Instant updatedAt;
    private List<BinaryContentDto> attachmentIds;
    private String content;

}
