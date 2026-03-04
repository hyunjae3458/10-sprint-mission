package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto toDto(User user, boolean online){
        if(user == null) return null;

        return new UserDto(user.getId()
                ,user.getName()
                ,user.getEmail()
                ,user.getProfileImageId()
                ,user.getCreatedAt()
                ,user.getUpdatedAt()
                ,online );
    }
}
