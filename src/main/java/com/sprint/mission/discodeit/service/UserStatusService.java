package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatusDto create(UserStatusCreateDto dto);
    UserStatusDto findUserStatus(UUID id);
    List<UserStatusDto>  findAll();
    UserStatusDto update(UUID userId, UserStatusUpdateRequest request);
    void delete(UUID id);
}
