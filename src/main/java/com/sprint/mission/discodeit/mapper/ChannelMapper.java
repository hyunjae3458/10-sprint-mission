package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public class ChannelMapper {
    private final MessageService messageService;
    private final UserMapper userMapper;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    public ChannelDto toDto(Channel channel){
        if(channel == null) return null;

        UUID channelId = channel.getId();

        // 가장 최근 메시지 찾기
        Message message = messageRepository.findFirstByChannelIdOrderByCreatedAtDesc(channelId)
                .orElseThrow(() -> new NoSuchElementException("해당 채널에 메시지가 존재하지 않습니다."));

        List<UserDto> participants = readStatusRepository.findAllByChannelId(channelId).stream()
                .map(readStatus -> userMapper.toDto(readStatus.getUser(),true))
                .toList();


        return new ChannelDto(channel.getId(),
                channel.getChannelName(),
                channel.getDescription(),
                participants,
                message.getCreatedAt(),
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
