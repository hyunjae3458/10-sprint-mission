package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface MessageService {
    MessageDto create(MessageCreateRequest request, List<MultipartFile> attachments);
    MessageDto findMessage(UUID messageId);
    PageResponse<Message> findAllMessagesByChannelId(UUID channelId, Pageable pageable);
    MessageDto update(UUID messageId, MessageUpdateRequest dto);
    void delete(UUID messageId);
}
