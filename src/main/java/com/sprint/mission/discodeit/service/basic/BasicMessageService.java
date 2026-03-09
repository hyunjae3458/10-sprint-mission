package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final MessageMapper messageMapper;
    private final PageResponseMapper pageResponseMapper;


    @Override
    @Transactional
    public MessageDto create(MessageCreateRequest request, List<MultipartFile> attachments) {
        User user = userRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new UserNotFoundException(request.getAuthorId()));
        Channel channel = channelRepository.findById(request.getChannelId())
                .orElseThrow(() -> new ChannelNotFoundException(request.getChannelId()));
        // 메시지 객체 생성
        Message message = new Message(user,request.getContent(),channel);
        if(attachments == null){
            attachments = new ArrayList<>();
        }

        attachments.forEach(bc -> {
            BinaryContent binaryContent;
            try {
                binaryContent = new BinaryContent(
                        bc.getSize(),
                        bc.getBytes(),
                        bc.getOriginalFilename(),
                        bc.getContentType());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            message.addAttachment(binaryContent);
            // 바이너리 컨텐츠는 조인 테이블의 casecade.All로 인해서 저장안해도됨
        });

        // 데이터에 정보 저장
        messageRepository.save(message);

        return messageMapper.toDto(message);
    }

    @Override
    @Transactional(readOnly = true)
    public MessageDto findMessage(UUID messageId) {
        Message message = getMessage(messageId);

        return messageMapper.toDto(message);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<MessageDto> findAllMessagesByChannelId(UUID channelId, Pageable pageable) {

        Page<MessageDto> messagePage = messageRepository.findAllByChannelId(channelId,pageable)
                .map(messageMapper::toDto);

        return  pageResponseMapper.fromPage(messagePage);
    }

    @Override
    public MessageDto update(UUID messageId, MessageUpdateRequest dto) {
        Message message = getMessage(messageId);
        message.updateMessage(dto.getNewContent());
        messageRepository.save(message);

        return messageMapper.toDto(message);
    }

    @Override
    @Transactional
    public void delete(UUID messageId) {
        Message message = getMessage(messageId);

        //데이터에서 메시지 삭제 -> 조인 테이블과의 영속성 전이로 인해 관련된 조인 테이블의 데이터도 삭제 -> binaryContent와 조인테이블
        // 과의 연결도 끊김 -> JPA는 조인 테이블과의 연결이 끊긴 데이터를 orphan으로 인식하고 삭제해버림(orphanRemoval)
        messageRepository.delete(message);
    }

    // 유효성 검사
    private Message getMessage(UUID messageId){
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("해당 메시자가 없습니다."));
    }

}
