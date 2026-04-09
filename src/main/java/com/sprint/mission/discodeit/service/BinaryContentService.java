package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContentDto create(BinaryContentCreateRequest dto);
    BinaryContentDto findBinaryContent(UUID id);
    List<BinaryContentDto> findAllIdIn(List<UUID> binaryContentIds);
    void delete(UUID id);
}
