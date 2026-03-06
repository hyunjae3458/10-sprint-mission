package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final ReadStatusMapper readStatusMapper;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    @Transactional
    public ReadStatusDto create(ReadStatusCreateRequest dto) {
        UUID userId = dto.getUserId();
        UUID channelId = dto.getChannelId();

        // userId, channelId가 null이라면 예외
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new ChannelNotFoundException(channelId));

        // 중복된게 없다면 생성
        if(!readStatusRepository.existsByUserIdAndChannelId(userId,channelId)){
            ReadStatus readStatus = new ReadStatus(user,channel);
            readStatusRepository.save(readStatus);
            // dto 반환
            return readStatusMapper.toDto(readStatus);
        }
        // 있다면 예외처리
        else {
            throw new IllegalStateException("이미 존재하는 읽음 상태가 있습니다.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ReadStatusDto findReadStatus(UUID id) {
        ReadStatus readStatus = getReadStatus(id);

        return readStatusMapper.toDto(readStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReadStatusDto> findAllByUserId(UUID userId) {
        List<ReadStatus> readStatusList = readStatusRepository.findAllByUserId(userId);
        return readStatusList.stream()
                                        .map(readStatusMapper::toDto)
                                        .toList();
    }

    @Override
    @Transactional
    public ReadStatusDto update(UUID id, Instant newLastReadAt) {
        ReadStatus readStatus = getReadStatus(id);
        readStatus.updateLastReadTime(newLastReadAt);

        return readStatusMapper.toDto(readStatus);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        readStatusRepository.deleteById(id);

    }

    private ReadStatus getReadStatus(UUID id){
        return readStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 읽기 상태는 없습니다."));
    }
}
