package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.file.FileUploadFailException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasicMessageServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    MessageRepository messageRepository;
    @Mock
    ChannelRepository channelRepository;
    @Mock
    MessageMapper messageMapper;
    @Mock
    BinaryContentStorage binaryContentStorage;
    @Mock
    BinaryContentRepository binaryContentRepository;
    @InjectMocks
    BasicMessageService messageService;

    // create
    @Test
    @DisplayName("메시지 생성 시 성공하여 messageDto 반환해야 한다")
    void create_message_success() {
        // given
        String content = "안녕하세요";
        UUID authorId = UUID.randomUUID();
        UUID channelId = UUID.randomUUID();

        MessageCreateRequest request = new MessageCreateRequest();
        request.setAuthorId(authorId);
        request.setChannelId(channelId);
        request.setContent(content);

        User user = new User("김현재", "fred@naver.com", "1234");
        Channel channel = new Channel("채널", "설명");

        byte[] mockByte = "바이트 데이터".getBytes();
        MockMultipartFile multipartFile = new MockMultipartFile(
                "attachment1", "dummy.png", "image/png", mockByte
        );
        List<MultipartFile> multipartFiles = List.of(multipartFile);

        MessageDto expectDto = new MessageDto(
                UUID.randomUUID(), null, channelId, Instant.now(), Instant.now(), new ArrayList<>(), content
        );
        BinaryContent binaryContent = new BinaryContent();
        UUID binaryContentId = UUID.randomUUID();
        ReflectionTestUtils.setField(binaryContent, "id", binaryContentId);

        when(userRepository.findById(authorId)).thenReturn(Optional.of(user));
        when(channelRepository.findById(channelId)).thenReturn(Optional.of(channel));

        when(binaryContentRepository.save(any(BinaryContent.class))).thenReturn(binaryContent);
        when(binaryContentStorage.put(binaryContentId,mockByte)).thenReturn(binaryContentId);
        when(messageRepository.save(any(Message.class))).thenReturn(new Message(user, content, channel));
        when(messageMapper.toDto(any(Message.class))).thenReturn(expectDto);

        // when
        MessageDto resultDto = messageService.create(request, multipartFiles);

        // then
        assertEquals(expectDto.getId(), resultDto.getId());
        assertEquals(content, resultDto.getContent());

        verify(binaryContentRepository, times(1)).save(any(BinaryContent.class));
        verify(binaryContentStorage, times(1)).put(any(), any());
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    @DisplayName("메시지 생성 시 유저id를 찾지못해 user not found 예외를 발생히야한다")
    void create_message_user_not_found_exception_fail() {
        // given
        UUID authorId = UUID.randomUUID();

        MessageCreateRequest request = new MessageCreateRequest();
        request.setAuthorId(authorId);

        when(userRepository.findById(request.getAuthorId())).thenReturn(Optional.empty());
        // when
        // then
        assertThrows(UserNotFoundException.class,
                () -> messageService.create(request,new ArrayList<>()));
        verify(channelRepository, never()).findById(any(UUID.class));
        verify(binaryContentRepository, never()).save(any(BinaryContent.class));
        verify(binaryContentStorage, never()).put(any(),any());
        verify(messageRepository, never()).save(any(Message.class));
        verify(messageMapper, never()).toDto(any(Message.class));
    }

    @Test
    @DisplayName("메시지 생성 시 채널id를 찾지 못해 channel not found 예외를 발생히야 한다")
    void create_message_channel_not_found_exception_fail() {
        UUID authorId = UUID.randomUUID();
        UUID channelId = UUID.randomUUID();

        MessageCreateRequest request = new MessageCreateRequest();
        request.setAuthorId(authorId);
        request.setChannelId(channelId);

        when(userRepository.findById(request.getAuthorId())).thenReturn(Optional.of(new User()));
        when(channelRepository.findById(request.getChannelId())).thenReturn(Optional.empty());
        // when
        // then
        assertThrows(ChannelNotFoundException.class,
                () -> messageService.create(request,new ArrayList<>()));
        verify(binaryContentRepository, never()).save(any(BinaryContent.class));
        verify(binaryContentStorage, never()).put(any(),any());
        verify(messageRepository, never()).save(any(Message.class));
        verify(messageMapper, never()).toDto(any(Message.class));
    }

    @Test
    @DisplayName("메시지 생성 시 파일 업로드에 실패하여 file upload failure 예외를 발생히야한다")
    void create_message_file_upload_exception_fail() {
        // given
        String content = "안녕하세요";
        UUID authorId = UUID.randomUUID();
        UUID channelId = UUID.randomUUID();

        MessageCreateRequest request = new MessageCreateRequest();
        request.setAuthorId(authorId);
        request.setChannelId(channelId);
        request.setContent(content);

        User user = new User("김현재", "fred@naver.com", "1234");
        Channel channel = new Channel("채널", "설명");

        byte[] mockByte = "바이트 데이터".getBytes();
        MockMultipartFile multipartFile = new MockMultipartFile(
                "attachment1", "dummy.png", "image/png", mockByte
        );
        List<MultipartFile> multipartFiles = List.of(multipartFile);

        BinaryContent binaryContent = new BinaryContent();
        UUID binaryContentId = UUID.randomUUID();
        ReflectionTestUtils.setField(binaryContent, "id", binaryContentId);

        when(userRepository.findById(authorId)).thenReturn(Optional.of(user));
        when(channelRepository.findById(channelId)).thenReturn(Optional.of(channel));
        when(binaryContentRepository.save(any(BinaryContent.class))).thenReturn(binaryContent);
        when(binaryContentStorage.put(binaryContentId,mockByte)).thenThrow(new FileUploadFailException());

        // when
        // then
        assertThrows(FileUploadFailException.class,
                () -> messageService.create(request, multipartFiles));

        verify(messageRepository, never()).save(any(Message.class));
        verify(messageMapper, never()).toDto(any(Message.class));
    }

    // update
    @Test
    @DisplayName("메시지 수정 시 성공하여 MessageDto 반환해야한다")
    void update_message_success() {
        // given
        UUID messageId = UUID.randomUUID();
        Message message = new Message(
                new User(),
                "안녕하세요",
                new Channel());
        ReflectionTestUtils.setField(message,"id",messageId);

        String content = "안녕!";
        MessageUpdateRequest request = new MessageUpdateRequest(content);

        MessageDto expectDto = new MessageDto(
                messageId,
                null,
                UUID.randomUUID(),
                Instant.now(),
                Instant.now(),
                new ArrayList<>(),
                request.getNewContent()
        );

        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));
        when(messageMapper.toDto(message)).thenReturn(expectDto);
        // when
        MessageDto resultDto = messageService.update(messageId,request);
        // then
        assertEquals(expectDto.getContent(),resultDto.getContent());
    }

    @Test
    @DisplayName("메시지 수정 시 messageid를 찾지 못해서 message not found 예외 발생히야한다")
    void update_message_not_found_exception_fail() {
        // given
        UUID messageId = UUID.randomUUID();

        String content = "안녕!";
        MessageUpdateRequest request = new MessageUpdateRequest(content);

        when(messageRepository.findById(messageId)).thenReturn(Optional.empty());
        // when
        // then
        assertThrows(MessageNotFoundException.class,
                () -> messageService.update(messageId, request));
        verify(messageMapper, never()).toDto(any(Message.class));
    }

    // delete
    @Test
    @DisplayName("메시지 삭제 시 성공 해야한다")
    void delete_message_success() {
        // given
        UUID messageId = UUID.randomUUID();
        Message message = new Message();

        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));
        // when
        messageService.delete(messageId);
        // then
        verify(messageRepository, times(1)).delete(any(Message.class));
    }

    @Test
    @DisplayName("메시지 삭제 시 messageid를 찾지 못해서 message not found 예외 발생히야한다")
    void delete_message_not_found_exception_fail() {
        // given
        UUID messageId = UUID.randomUUID();

        when(messageRepository.findById(messageId)).thenReturn(Optional.empty());
        // when
        // then
        assertThrows(MessageNotFoundException.class,
                () -> messageService.delete(messageId));
        verify(messageRepository, never()).delete(any(Message.class));
    }
}