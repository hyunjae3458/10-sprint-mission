package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.response.ChannelResponseDto;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    ChannelDto createPublic(PublicChannelCreateRequest request);
    ChannelDto createPrivate(PrivateChannelCreateRequest request);
    ChannelDto joinUsers(UUID channelId, UUID ...userId);
    ChannelDto findChannel(UUID channelId);
    List<ChannelDto> findAllChannelsByUserId(UUID userId);
    List<ChannelDto> findAllChannels();
    ChannelDto update(UUID channelId, PublicChannelUpdateRequest dto);
    void delete(UUID channelId);
}
