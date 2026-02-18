package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.response.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.response.ChannelResponsePrivateDto;
import com.sprint.mission.discodeit.dto.channel.response.ChannelResponsePublicDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.MessageRepository;
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
                messageService.findLatestMessageByChannelId(channel.getId()),
                channel.getChannelType());
    }
}
