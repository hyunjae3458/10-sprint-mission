package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.type.ChannelType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "channels")
public class Channel extends BaseUpdatableEntity {
    @Column(name = "channel_name")
    private String channelName;

    @Enumerated(EnumType.STRING)
    @Column(name = "channel_type", nullable = false)
    private ChannelType channelType;

    @Column(name = "description")
    private String description;

    public Channel(String channelName, String description) {
        this.channelName = channelName;
        this.description = description;
    }

    public void updateChannel(String newChannelName, String newDescriptionn) {
        this.channelName = newChannelName;
        this.description = newDescriptionn;
    }
}