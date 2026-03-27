package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.file.FileUploadFailException;
import com.sprint.mission.discodeit.exception.user.DuplicateEmailFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasicUserServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    UserStatusRepository userStatusRepository;
    @Mock
    UserMapper userMapper;
    @Mock
    BinaryContentRepository binaryContentRepository;
    @Mock
    BinaryContentStorage binaryContentStorage;
    @InjectMocks
    BasicUserService userService;


    @Test
    @DisplayName("프로필 이미지 없이 회원가입 진행시 정상적으로 성공하여 userDto 반환")
    void sign_up_without_profile_success() {
        // given
        // 기대값
        String username = "김현재";
        String email = "fred@naver.com";
        String password = "123123";
        // 기대 유저 객체 생성
        User user = new User(username, email, password);
        // 기대 유저 생성 요청 dto 생성
        UserCreateRequest request = new UserCreateRequest();
        request.setEmail("fred@naver.com");
        request.setUsername("김현재");
        request.setPassword("123123");
        // 기대 유저 상태 생성
        UserStatus userStatus = new UserStatus(user);
        UserDto expectDto = new UserDto(user.getId(), username, email, null, true);

        // 동작 실행시 기대값이 나오도록 함
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userStatusRepository.save(any(UserStatus.class))).thenReturn(userStatus);
        when(userMapper.toDto(any(User.class),eq(true))).thenReturn(expectDto);
        // when
        UserDto resultDto = userService.create(request,null);
        // then
        // dto가 널이 아닌지, 필드값이 일치한지 테스트
        assertNotNull(resultDto);
        assertEquals(expectDto.getId(), resultDto.getId());
        assertEquals(expectDto.getUsername(), resultDto.getUsername());
        assertEquals(expectDto.getEmail(), resultDto.getEmail());
        assertEquals(expectDto.getProfile(), resultDto.getProfile());
        assertTrue(resultDto.isOnline());
    }

    @Test
    @DisplayName("프로필 이미지 있이 회원가입 진행시 정상적으로 성공하여 userDto 반환")
    void sign_up_with_profile_success() {
        // given
        // 유저 객체 기대필드값
        String username = "김현재";
        String email = "fred@naver.com";
        String password = "123123";
        // 기대 바이트값
        byte[] mockByte = "바이트 데이터".getBytes();
        // 기대 유저 객체 생성
        User user = new User(username, email, password);
        // 기대 유저 생성 요청 dto 생성
        UserCreateRequest request = new UserCreateRequest();
        request.setEmail("fred@naver.com");
        request.setUsername("김현재");
        request.setPassword("123123");

        // 기대 멀티 파일 생성
        MockMultipartFile multipartFile = new MockMultipartFile(
                "profile",
                "dumi_file.jpg",
                "image/jpg",
                mockByte);
        // 기대 바이너리 컨텐츠 생성
        UUID fakeId = UUID.randomUUID();
        BinaryContent bc = new BinaryContent(
                multipartFile.getSize(),
                multipartFile.getOriginalFilename(),
                multipartFile.getContentType());
        // 기대 바이너리 dto 생성
        BinaryContentDto bcDto = new BinaryContentDto(fakeId, bc.getSize(), bc.getFileName(), bc.getContentType());
        // 기대 유저 상태 생성
        UserStatus userStatus = new UserStatus(user);
        // 기대 유저 dto 생성
        UserDto expectDto = new UserDto(user.getId(), username, email, bcDto, true);

        // 동작 실행시 기대값이 나오도록 함
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(binaryContentRepository.save(any(BinaryContent.class))).thenReturn(bc);
        when(binaryContentStorage.put(bc.getId(),mockByte)).thenReturn(bc.getId());
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userStatusRepository.save(any(UserStatus.class))).thenReturn(userStatus);
        when(userMapper.toDto(any(User.class),eq(true))).thenReturn(expectDto);

        // when
        UserDto resultDto = userService.create(request, multipartFile);
        // then
        assertNotNull(resultDto);
        assertEquals(expectDto.getId(), resultDto.getId());
        assertEquals(expectDto.getUsername(), resultDto.getUsername());
        assertEquals(expectDto.getEmail(), resultDto.getEmail());
        assertEquals(expectDto.getProfile(), resultDto.getProfile());
        assertTrue(resultDto.isOnline());
        assertEquals(expectDto.getProfile().getId(), resultDto.getProfile().getId());
        assertEquals(expectDto.getProfile().getSize(), resultDto.getProfile().getSize());
        assertEquals(expectDto.getProfile().getFileName(), resultDto.getProfile().getFileName());
        assertEquals(expectDto.getProfile().getContentType(), resultDto.getProfile().getContentType());
    }

    @Test
    @DisplayName("이메일 중복으로 회원가입 진행시 실패하여 duplicateEmail 예외 발생")
    void sign_up_duplicate_email_exception_fail() {
        // given
        String username = "김현재";
        String email = "fred@naver.com";
        String password = "123123";

        UserCreateRequest request = new UserCreateRequest();
        request.setEmail(email);
        request.setUsername(username);
        request.setPassword(password);

        // 동작 실행시 기대값이 나오도록 함
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        // when
        // then
        assertThrows(DuplicateEmailFoundException.class,
        () -> userService.create(request, null));
        verify(userRepository, never()).save(any(User.class));
        verify(userStatusRepository, never()).save(any(UserStatus.class));
    }

    @Test
    @DisplayName("프로필 이미지 스토리지 저장 중 에러(IOException 등)가 발생하면, FileUploadFailException 예외가 발생한다.")
    void upload_profile_exception_fail(){
        // given
        // 유저 객체 기대필드값
        String username = "김현재";
        String email = "fred@naver.com";
        String password = "123123";
        // 기대 바이트값
        byte[] mockByte = "바이트 데이터".getBytes();
        // 기대 유저 객체 생성
        User user = new User(username, email, password);
        // 기대 유저 생성 요청 dto 생성
        UserCreateRequest request = new UserCreateRequest();
        request.setEmail("fred@naver.com");
        request.setUsername("김현재");
        request.setPassword("123123");

        // 기대 멀티 파일 생성
        MockMultipartFile multipartFile = new MockMultipartFile(
                "profile",
                "dumi_file.jpg",
                "image/jpg",
                mockByte);
        // 기대 바이너리 컨텐츠 생성
        BinaryContent bc = new BinaryContent(
                multipartFile.getSize(),
                multipartFile.getOriginalFilename(),
                multipartFile.getContentType());

        // 동작 실행시 기대값이 나오도록 함
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(binaryContentRepository.save(any(BinaryContent.class))).thenReturn(bc);
        when(binaryContentStorage.put(bc.getId(),mockByte))
                .thenThrow(new FileUploadFailException());
        // when
        // then
        assertThrows(FileUploadFailException.class,
                () -> userService.create(request, multipartFile));
    }
}