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
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;


@Mapper(componentModel = "spring")
public abstract class ChannelMapper {
    protected   UserMapper userMapper;
    protected  ReadStatusRepository readStatusRepository;
    protected  MessageRepository messageRepository;

    @Mapping(target = "lastMessageAt", expression = "java(getLatestMessageTime(channel.getId()))")
    @Mapping(target = "participants", expression = "java(getParticipants(channel.getId()))")
    public abstract ChannelDto toDto(Channel channel);

    protected Instant getLatestMessageTime(UUID channelId){
        // 가장 최근 메시지 찾기
        Message message = messageRepository.findFirstByChannelIdOrderByCreatedAtDesc(channelId)
                .orElseThrow(() -> new NoSuchElementException("해당 채널에 메시지가 존재하지 않습니다."));

        return message.getCreatedAt();
    }

    protected List<UserDto> getParticipants(UUID channelId){
        return readStatusRepository.findAllByChannelId(channelId).stream()
                .map(readStatus -> userMapper.toDto(readStatus.getUser(),true))
                .toList();
    }
}
