package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;

    @Override
    @Transactional
    public UserDto create(UserCreateRequest request, MultipartFile profile) {
        // 유저 객체 생성
        log.info("유저 회원가입 요청: 유저 이름 = {}, 이메일 = {}", request.getUsername(), request.getEmail());

        // 이메일 중복 확인
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("회원가입 실패 - 이미 사용 중인 이메일: {}", request.getEmail());
            // 예외처리 고도화 필요
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        User user = new User(request.getUsername(),
                request.getEmail(),
                request.getPassword());

        // 프로필 등록 여부 & binaryContent객체 생성
        if(profile != null){
            try{
                BinaryContent binaryContent = new BinaryContent(
                    profile.getSize(),
                    profile.getOriginalFilename(),
                    profile.getContentType());

                user.addProfileImage(binaryContent);
                // 연관성 주입
                binaryContentRepository.save(binaryContent);
                binaryContentStorage.put(binaryContent.getId(), profile.getBytes());
                log.info("파일 업로드 성공! email = {}, 파일 id = {}, 파일 이름 = {}",
                        request.getEmail(), binaryContent.getId(), binaryContent.getFileName());

            } catch (Exception e) {
                log.error("파일 업로드 실패: email = {}, 파일 이름 = {}",request.getEmail(), profile.getOriginalFilename(),e);
                throw new RuntimeException("파일 업로드 오류가 발생했습니다",e);
            }
        }
        // 유저 저장
        userRepository.save(user);

        // 유저 상태 생성
        UserStatus userStatus = new UserStatus(user);
        userStatusRepository.save(userStatus);
        log.info("유저 회원가입 성공: userId = {}", user.getId());

        return userMapper.toDto(user,true);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findUser(UUID userId) {
        User user = getUser(userId);
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자 상태가 없습니다."));

        boolean online = isOnline(userStatus.getLastActiveAt());
        log.debug("유저 조회 성공: 사용자 id = {}, 이름 = {}, 온라인 상태 = {}",user.getId(), user.getUsername(), online);
        return userMapper.toDto(user,online);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAllUsers() {
        List<User> userList = userRepository.findAll();
        log.debug("전체 유저 목록 조회 요청: 조회된 유저 수 = {}", userList.size());
        if(!userList.isEmpty()){
            // 가져온 객체들을 dto로 변환
            return userList.stream()
                    .map(user -> {
                        // n + 1 문제 지점
                        boolean online = userStatusRepository.findByUserId(user.getId())
                                .map(us -> isOnline(us.getLastActiveAt()))
                                .orElseThrow(() -> new NoSuchElementException("해당 유저상태는 없습니다."));
                        return userMapper.toDto(user, online);
                    })
                    .toList();
        } else{
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional
    public UserDto update(UUID userId, UserUpdateRequest request, MultipartFile profile) {
        User user = getUser(userId);
        // 이름 수정
        if(request.getNewUsername() != null){
            user.updateName(request.getNewUsername());
        }
        // 이메일 수정
        if(request.getNewEmail() != null){
            if(userRepository.existsByEmail(request.getNewEmail())){
                log.warn("회원가입 실패 - 이미 사용 중인 이메일: {}", request.getNewEmail());
                throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
            }
            user.updateEmail(request.getNewEmail());
        }
        // 비밀번호 수정
        if(request.getNewPassword() != null){
            user.updatePassword(request.getNewPassword());
        }
        // 프로필 수정(기존에 있던 binaryContent를 삭제하고 업데이트 dto에 있는 binaryContent를 생성
        if(profile != null){
            if(user.getProfile() != null) binaryContentRepository.delete(user.getProfile());
            try{
                BinaryContent newBinaryContent = new BinaryContent(
                        profile.getSize(),
                        profile.getOriginalFilename(),
                        profile.getContentType());
                binaryContentRepository.save(newBinaryContent);
                binaryContentStorage.put(newBinaryContent.getId(), profile.getBytes());
                log.info("프로필 수정 성공: 유저 id = {}, 파일 id = {}, 파일 이름 = {}"
                        , userId, newBinaryContent.getId(), newBinaryContent.getFileName());

                user.updateProfileImg(newBinaryContent);
            } catch (Exception e) {
                log.error("파일 업로드 실패: 유저 id = {}, 파일 이름 = {}", userId, profile.getOriginalFilename(),e);
                throw new RuntimeException("파일 업로드 오류가 발생했습니다",e);
            }
        }
        log.info("유저 정보 수정 성공: 유저 id = {}", userId);
        return findUser(userId);
    }

    @Override
    @Transactional
    public void delete(UUID userId) {
        User user = getUser(userId);
        BinaryContent profileImg = user.getProfile();

        // 유저를 데이터에서 삭제
        userRepository.delete(user);
        log.info("유저 삭제 성공: 유저 id = {}", userId);
        // 유저가 들고 있던 바이너리 컨텐츠 삭제
        if( profileImg != null){
            binaryContentRepository.delete(profileImg);
        }
    }

    private boolean isOnline(Instant lastOnlineAt){
        // 만약 최종접속시간이 현재시간의 5분전 이내라면 참 반환
        return lastOnlineAt.isAfter(Instant.now().minus(Duration.ofMinutes(5)));
    }

    // 유효성 검사
    private User getUser(UUID userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자가 없습니다."));
    }
}
