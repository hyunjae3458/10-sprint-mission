package com.sprint.mission.discodeit.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserDto {
    private UUID id;
    private String username;
    private String email;
    private UUID profileId;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean online;

    @Override
    public String toString() {
        return "UserResponseDto{" +
                "userId=" + id +
                ", name='" + username + '\'' +
                ", email='" + email + '\'' +
                ", profileImageId=" + profileId +
                ", online=" + online +
                '}';
    }
}
