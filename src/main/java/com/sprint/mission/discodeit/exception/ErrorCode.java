package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    USER_NOT_FOUND(404,"USER_NOT_FOUND","유저를 찾을 수 없습니다."),
    DUPLICATE_EMAIL(409,"DUPLICATE_EMAIL","중복된 이메일입니다."),
    CHANNEL_NOT_FOUND(404,"CHANNEL_NOT_FOUND","채널을 찾을 수 없습니다."),
    PRIVATE_CHANNEL_UPDATE(405, "PRIVATE_CHANNEL_UPDATE", "개인 채널은 수정할 수 없습니다."),
    WRONG_PASSWORD( 401,"WRONG_PASSWORD","잘못된 비밀번호입니다.");

    private final int status;
    private final String code;
    private final String message;
}
