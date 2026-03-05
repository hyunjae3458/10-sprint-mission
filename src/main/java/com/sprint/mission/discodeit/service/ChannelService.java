package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.*;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    ChannelCreateResponse createPublic(PublicChannelCreateRequest request);
    ChannelCreateResponse createPrivate(PrivateChannelCreateRequest request);
    ChannelDto findChannel(UUID channelId);
    List<ChannelDto> findAllChannelsByUserId(UUID userId);
    List<ChannelDto> findAllChannels();
    ChannelDto update(UUID channelId, PublicChannelUpdateRequest dto);
    void delete(UUID channelId);
}
