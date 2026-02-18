package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
    Optional<Message> findById(UUID messageId);
    List<Message> findAll();
    Optional<Message> findLatestByChannelId(UUID channelId);
    void save(Message message);
    void delete(UUID messageId);
}
