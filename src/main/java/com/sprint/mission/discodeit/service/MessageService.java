package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface MessageService {
    MessageDto create(MessageCreateRequest request, List<MultipartFile> attachments);
    MessageDto findMessage(UUID messageId);
    Instant findLatestMessageByChannelId(UUID channelId);
    List<MessageDto> findMessageByKeyword(UUID channelId, String keyword);
    List<MessageDto> findAllMessagesByChannelId(UUID channelId);
    MessageDto update(UUID messageId, MessageUpdateRequest dto);
    void delete(UUID messageId);
}
