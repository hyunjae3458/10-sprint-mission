package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MessageMapper {
    private final UserMapper userMapper;
    private final BinaryContentMapper binaryContentMapper;

    public MessageDto toDto(Message message){
        if(message == null) return null;

        List<BinaryContentDto> binaryContentDtoList = message.getAttachments().stream()
                .map(binaryContentMapper::toDto)
                .toList();

        return new MessageDto(message.getId(),
                userMapper.toDto(message.getAuthor(),true),
                message.getChannel().getId(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                binaryContentDtoList,
                message.getText());
    }
}
