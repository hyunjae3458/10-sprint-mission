package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.authDto.UserLoginRequestDto;
import com.sprint.mission.discodeit.dto.authDto.UserLoginResponseDto;

public interface AuthService {
    UserLoginResponseDto login(UserLoginRequestDto dto);
}
