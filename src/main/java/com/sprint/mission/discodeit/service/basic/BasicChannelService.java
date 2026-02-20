package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.type.ChannelType;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ReadStatusRepository readStatusRepository;
    private final ChannelMapper channelMapper;

    @Override
    // 공용 채널
    public ChannelCreateResponse createPublic(PublicChannelCreateRequest request) {
        // 채널 생성
        Channel channel = new Channel(request.getName(), request.getDescription());
        channel.setChannelType(ChannelType.PUBLIC);
        // 채널 저장
        channelRepository.save(channel);
        return channelMapper.toCreateResponse(channel);
    }

    @Override
    public ChannelCreateResponse createPrivate(PrivateChannelCreateRequest request) {
        // 채널 생성
        Channel channel = new Channel();
        // 입력으로 들어온 유저 당 readStatus도 생성 후 저장
        request.getParticipantIds()
                .forEach(userId ->{
                    channel.addUsers(userId);
                    readStatusRepository.save(new ReadStatus(userId, channel.getId()));
                });

        channel.setChannelType(ChannelType.PRIVATE);
        channelRepository.save(channel);
        return channelMapper.toCreateResponse(channel);
    }

    @Override
    public ChannelDto joinUsers(UUID channelId, UUID... userId) {
        Channel channel = getChannel(channelId);
        // 만약 채널이 프라이빗이라면 예외출력
        if(channel.getChannelType() == ChannelType.PRIVATE){
            throw new IllegalStateException("불가! 이 채널은 Private 채널입니다");
        }

        Arrays.stream(userId)
                .map(id -> userRepository.findById(id)
                        .orElseThrow(() -> new NoSuchElementException("해당 유저가 없습니다")))
                .forEach(user ->
                {
                    channel.addUsers(user.getId());
                    readStatusRepository.save(new ReadStatus(user.getId(), channel.getId()));
                });
        channelRepository.save(channel);
        return channelMapper.toDto(channel);
    }

    @Override
    public ChannelDto findChannel(UUID channelId) {
        Channel channel = getChannel(channelId);
        return channelMapper.toDto(channel);
    }

    @Override
    public List<ChannelDto> findAllChannels() {
        List<Channel> channelList = channelRepository.findAll();
        List<ChannelDto> dtoList = channelList.stream()
                .map(channelMapper::toDto)
                .toList();
        return dtoList;
    }

    @Override
    public List<ChannelDto> findAllChannelsByUserId(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자가 없습니다."));

        List<ReadStatus> readStatusList = readStatusRepository.findAllByUserId(userId);
        // 공용채널 조회
        List<ChannelDto> publicList = readStatusList.stream()
                .map(readStatus ->
                    getChannel(readStatus.getChannelId()))
                .filter(channel -> channel.getChannelType() == ChannelType.PUBLIC)
                .map(channelMapper::toDto)
                .toList();

        // 개인채널 조회
        List<ChannelDto> privateList = readStatusList.stream()
                .filter(readStatus -> readStatus.getUserId().equals(userId))
                .map(readStatus ->
                        getChannel(readStatus.getChannelId()))
                .filter(channel -> channel.getChannelType() == ChannelType.PRIVATE)
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
        if(channel.getChannelType() == ChannelType.PRIVATE){
            throw new IllegalStateException("Private 채널은 수정할 수 없습니다");
        }
        // 채널 업데이트
        channel.updateChannel(dto.getNewName(),dto.getNewDescription());
        // 채널 갱신
        channelRepository.save(channel);

        return channelMapper.toDto(channel);
    }

    @Override
    public void delete(UUID channelId) {
        Channel channel = getChannel(channelId);

        // 채널이 삭제될때 채널에 속해있던 메시지들 전부 삭제
        List<UUID> messageList = new ArrayList<>(channel.getMessageList());
        messageList.stream().map(messageRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(message ->{
                messageRepository.delete(message.getId());
                // 유저가 가지고 있던 메시지도 삭제
                User author = userRepository.findById(message.getUserId())
                        .orElseThrow(() -> new NoSuchElementException("해당 사용자가 없습니다."));
                if(author != null){
                    author.getMessageList().remove(message.getId());
                    // 정보 갱신
                    userRepository.save(author);
                }
                // 정보 갱신
                messageRepository.save(message);
        });

        // 채널과 연관된 ReadStatus도 삭제
        List<ReadStatus> readStatusList = readStatusRepository.findAllByChannelId(channelId);
        readStatusList.forEach(readStatus -> readStatusRepository.delete(readStatus.getId()));

        channelRepository.delete(channelId);
    }

    private Channel getChannel(UUID channelId){
        return channelRepository.findById(channelId)
                .orElseThrow(()->new NoSuchElementException("해당 채널을 찾을 수 없습니다"));
    }
}
