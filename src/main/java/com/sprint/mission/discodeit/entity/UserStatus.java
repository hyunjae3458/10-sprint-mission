package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
@Table(name = "user_statuses")
@NoArgsConstructor
public class UserStatus extends BaseEntity{
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", unique = true)
    private  User user;

    @Column(name = "last_online_at")
    private Instant lastOnlineAt;

    public UserStatus(User user){
        this.user = user;
        this.lastOnlineAt = Instant.now();
    }

    // 최신 접속 시간 갱신
    public void updateAccessTime(Instant newLastActivateAt){
        this.lastOnlineAt = newLastActivateAt;
    }

}
