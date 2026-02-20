package com.sprint.mission.discodeit.dto.binaryContent;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class BinaryContentCreateRequest {
    private long size;
    private String name;
    private String contentType;
    private byte[] fileData;
}
