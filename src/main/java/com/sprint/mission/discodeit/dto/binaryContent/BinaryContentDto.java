package com.sprint.mission.discodeit.dto.binaryContent;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class BinaryContentDto {
    private UUID id;
    private Instant createdAt;
    private byte[] bytes;
    private long size;
    private String fileName;
    private String contentType;
}
