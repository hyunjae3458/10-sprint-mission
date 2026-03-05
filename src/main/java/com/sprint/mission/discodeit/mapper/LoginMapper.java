package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.authDto.UserLoginResponseDto;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.stereotype.Component;

@Component
public class LoginMapper {
    public UserLoginResponseDto toDto(User user, boolean online){
        if(user == null) return null;

        return new UserLoginResponseDto(user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getProfileImg().getId());
    }
}
