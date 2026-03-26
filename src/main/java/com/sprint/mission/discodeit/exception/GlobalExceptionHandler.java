package com.sprint.mission.discodeit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e){
        if(e.getUserId() != null){
            log.warn("유저 조회 실패: 조회 유저 id = {}",e.getUserId());
        } else {
            log.warn("유저 로그인 실패: 로그인 유저 이름 = {}", e.getUsername());
        }
        ErrorResponse response = new ErrorResponse("USER_NOT_FOUND", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(WrongPasswordException.class)
    public ResponseEntity<ErrorResponse> handleWrongPasswordException(WrongPasswordException e){
        ErrorResponse response = new ErrorResponse("WRONG_PASSWORD",e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ChannelNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleChannelNotFoundException(ChannelNotFoundException e){
        ErrorResponse response = new ErrorResponse("CHANNEL_NOT_FOUND",e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(DuplicateEmailFoundException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEmailFoundException(DuplicateEmailFoundException e){
        log.warn("유저 회원가입 실패: 이미 사용 중인 이메일 = {}", e.getEmail());
        ErrorResponse response = new ErrorResponse("DUPLICATE_EMAIL", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
