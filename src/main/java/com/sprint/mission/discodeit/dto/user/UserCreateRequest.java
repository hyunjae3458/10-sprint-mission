package com.sprint.mission.discodeit.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
@Setter
public class UserCreateRequest {
    private String name;
    private String email;
    private String password;
}
