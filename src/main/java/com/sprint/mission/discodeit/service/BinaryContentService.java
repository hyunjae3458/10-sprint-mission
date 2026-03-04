package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContentDto create(BinaryContentCreateRequest dto);
    BinaryContentDto findId(UUID id);
//    BinaryContentDto findBinaryContentByUserId(UUID id);
    List<BinaryContentDto> findAllIdIn(List<UUID> binaryContentId);
//    List<BinaryContent> findAllByMessageId(UUID messageId);
    void delete(UUID id);
}
