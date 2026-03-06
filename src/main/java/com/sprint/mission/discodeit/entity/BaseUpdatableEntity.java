package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@MappedSuperclass
@Getter
public class BaseUpdatableEntity extends BaseEntity{

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;
}
