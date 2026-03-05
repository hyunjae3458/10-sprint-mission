package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final BinaryContentMapper binaryContentMapper;
    public UserDto toDto(User user, boolean online){
        if(user == null) return null;
        BinaryContentDto binaryContentDto = binaryContentMapper.toDto(user.getProfileImg());

        return new UserDto(user.getId()
                ,user.getName()
                ,user.getEmail()
                ,binaryContentDto
                ,user.getCreatedAt()
                ,user.getUpdatedAt()
                ,online);
    }
}
