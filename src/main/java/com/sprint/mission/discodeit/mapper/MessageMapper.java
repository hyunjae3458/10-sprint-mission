package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Mapper(componentModel = "spring", uses = {BinaryContentMapper.class})
public abstract class MessageMapper {
    @Autowired
    protected BinaryContentMapper binaryContentMapper;

    @Mapping(target = "attachments", expression = "java(getBinaryContentDtoList(message))")
    public abstract MessageDto toDto(Message message);

    protected List<BinaryContentDto> getBinaryContentDtoList(Message message){
        return message.getAttachments().stream()
                .map(binaryContentMapper::toDto)
                .toList();
    }
}
