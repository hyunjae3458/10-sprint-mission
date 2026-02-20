package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "User API")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 유저 생성
    @RequestMapping(method = RequestMethod.POST,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto> postUser(@RequestPart("userCreateRequest") UserCreateRequest request,
                                          @RequestPart(value = "profile", required = false)MultipartFile profile){
        UserDto response = userService.create(request , profile);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 유저 단건 조회
//    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
//    public ResponseEntity<UserDto> getUser(@PathVariable("userId") UUID id){
//        UserDto response = userService.findUser(id);
//        return ResponseEntity.ok(response);
//    }

    // 유저 전체 조회
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserDto>> getAll(){
        List<UserDto> userList = userService.findAllUsers();
        return ResponseEntity.ok(userList);
    }

    // 유저 업데이트
    @RequestMapping(value = "/{userId}", method = RequestMethod.PATCH, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto> updateUser(@PathVariable("userId") UUID id,
                                              @RequestPart("userUpdateRequest") UserUpdateRequest request,
                                              @RequestPart(value="profile", required = false) MultipartFile profile){
        UserDto response = userService.update(id,request, profile);
        return ResponseEntity.ok(response);
    }

    // 유저 온라인상태 업데이트
    @RequestMapping(value = "/{userId}/userStatus" , method = RequestMethod.PATCH)
    public ResponseEntity<Void> updateUserStatusByUserId(@PathVariable("userId") UUID id,
                                                         @RequestBody(required = true) UserStatusUpdateRequest request){
        userService.updateOnlineStatus(id,request);
        return ResponseEntity.ok().build();
    }

    // 유저 삭제
    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") UUID id){
        userService.delete(id);
        return ResponseEntity.noContent().build(); // noContent는 객체를 만든다는 뜻, build를 붙혀서 객체 만듬

    }

}
