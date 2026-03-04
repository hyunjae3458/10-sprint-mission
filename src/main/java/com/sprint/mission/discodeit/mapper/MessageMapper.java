package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {
    public MessageDto toDto(Message message){
        if(message == null) return null;

        return new MessageDto(message.getId(),
                message.getUserId(),
                message.getChannelId(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                message.getBinaryContentList(),
                message.getText());
    }
}
