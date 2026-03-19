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
public class UserStatus extends BaseUpdatableEntity{
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", unique = true)
    private  User user;

    @Column(name = "last_active_at")
    private Instant lastActiveAt;

    public UserStatus(User user){
        this.user = user;
        this.lastActiveAt = Instant.now();
    }

    // 최신 접속 시간 갱신
    public void updateAccessTime(Instant newLastActivateAt){
        this.lastActiveAt = newLastActivateAt;
    }

}
