package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Entity
@Table(
        name = "read_statuses",
        // 제약 조건 추가
        uniqueConstraints = {
                @UniqueConstraint(
                        // 제약조건 이름
                        name = "unique_author_channel",
                        // 제약조건 테이블의 컬럼 이름
                        columnNames = { "author_id", "channel_id" }
                )
        }
)
@NoArgsConstructor
public class ReadStatus extends BaseEntity{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @Column(name = "last_read_time", nullable = false)
    private Instant lastReadTime;

    public ReadStatus(User user, Channel channel){
        this.user = user;
        this.channel = channel;
        this.lastReadTime = Instant.now();
    }

    public void updateLastReadTime(){
        this.lastReadTime = Instant.now();
    }
}