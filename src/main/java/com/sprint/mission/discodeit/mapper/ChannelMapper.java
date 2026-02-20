package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ChannelMapper {
    private final MessageService messageService;

    public ChannelDto toDto(Channel channel){
        if(channel == null) return null;

        return new ChannelDto(channel.getId(),
                channel.getChannelName(),
                channel.getDescription(),
                channel.getUserList(),
                channel.getLastMessageAt(),
                channel.getChannelType());
    }

    public ChannelCreateResponse toCreateResponse(Channel channel){
        if(channel == null) return null;

        return new ChannelCreateResponse(channel.getId(),
                channel.getCreatedAt(),
                channel.getUpdatedAt(),
                channel.getChannelType(),
                channel.getChannelName(),
                channel.getDescription());
    }
}
