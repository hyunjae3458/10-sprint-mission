package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.authDto.UserLoginRequestDto;
import com.sprint.mission.discodeit.dto.user.UserDto;

public interface AuthService {
    UserDto login(UserLoginRequestDto dto);
}
