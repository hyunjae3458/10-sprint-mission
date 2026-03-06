package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component

@Mapper(componentModel = "spring")
public interface UserStatusMapper {
    UserStatusDto toDto(UserStatus userStatus);
}
