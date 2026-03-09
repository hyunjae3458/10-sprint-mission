package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.type.ChannelType;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ReadStatusRepository readStatusRepository;
    private final ChannelMapper channelMapper;

    // 공용 채널
    @Override
    @Transactional
    public ChannelDto createPublic(PublicChannelCreateRequest request) {
        // 채널 생성
        Channel channel = new Channel(request.getName(), request.getDescription());
        channel.setType(ChannelType.PUBLIC);
        // 채널 저장
        channelRepository.save(channel);
        return channelMapper.toDto(channel);
    }

    //개인 채널
    @Override
    @Transactional
    public ChannelDto createPrivate(PrivateChannelCreateRequest request) {
        // 채널 생성
        Channel channel = new Channel();

        channel.setType(ChannelType.PRIVATE);
        channelRepository.save(channel);

        // 입력으로 들어온 유저 당 readStatus도 생성 후 저장
        request.getParticipantIds().stream()
                .map(id -> userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id)))
                .forEach(user -> readStatusRepository.save(new ReadStatus(user, channel)));
        return channelMapper.toDto(channel);
    }

    @Override
    @Transactional(readOnly = true)
    public ChannelDto findChannel(UUID channelId) {
        Channel channel = getChannel(channelId);
        return channelMapper.toDto(channel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChannelDto> findAllChannels() {
        List<Channel> channelList = channelRepository.findAll();
        return channelList.stream()
                .map(channelMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChannelDto> findAllChannelsByUserId(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        List<ReadStatus> readStatusList = readStatusRepository.findAllByUserId(userId);
        // 공용채널 조회
        List<ChannelDto> publicList = channelRepository.findAll().stream()
                .filter(channel -> channel.getType() == ChannelType.PUBLIC)
                .map(channelMapper::toDto)
                .toList();

        // 개인채널 조회
        List<ChannelDto> privateList = readStatusList.stream()
                .filter(readStatus -> readStatus.getUser().equals(user))
                .map(readStatus -> channelRepository.findById(readStatus.getChannel().getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(channel -> channel.getType() == ChannelType.PRIVATE)
                .map(channelMapper::toDto)
                .toList();

        // 반환할 리스트
        List<ChannelDto> channelList = new ArrayList<>();
        // 합친 후 반환
        channelList.addAll(publicList);
        channelList.addAll(privateList);

        return channelList;
    }

    @Override
    public ChannelDto update(UUID channelId, PublicChannelUpdateRequest dto) {
        Channel channel = getChannel(channelId);
        if(channel.getType() == ChannelType.PRIVATE){
            throw new IllegalStateException("Private 채널은 수정할 수 없습니다");
        }
        // 채널 업데이트
        channel.updateChannel(dto.getNewName(),dto.getNewDescription());
        // 채널 갱신
        channelRepository.save(channel);

        return channelMapper.toDto(channel);
    }

    @Override
    @Transactional
    public void delete(UUID channelId) {
        Channel channel = getChannel(channelId);

        // 메시지 관련 바이너리 파일들도 삭제하기 위해 메시지를 먼저 삭제한다
        List<Message> messages = messageRepository.findAllByChannel(channel);
        messageRepository.deleteAll(messages);

        channelRepository.delete(channel);
    }

    private Channel getChannel(UUID channelId){
        return channelRepository.findById(channelId)
                .orElseThrow(()->new NoSuchElementException("해당 채널을 찾을 수 없습니다"));
    }
}
