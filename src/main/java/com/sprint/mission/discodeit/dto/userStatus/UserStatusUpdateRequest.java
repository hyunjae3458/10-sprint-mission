package com.sprint.mission.discodeit.dto.userStatus;

import lombok.Getter;

import java.time.Instant;

@Getter
public class UserStatusUpdateRequest {
    private Instant newLastActiveAt;
}
