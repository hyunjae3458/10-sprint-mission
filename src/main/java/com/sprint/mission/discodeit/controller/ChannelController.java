package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.dto.channel.response.ChannelResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
@Tag(name = "Channel", description = "Channel API")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;

    // 공용 채널 생성
    @RequestMapping(value = "/public",method = RequestMethod.POST)
    public ResponseEntity<ChannelDto> postPublicChannel(@RequestBody PublicChannelCreateRequest request){
        ChannelDto response = channelService.createPublic(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    // 사설 채널 생성
    @RequestMapping(value = "/private",method = RequestMethod.POST)
    public ResponseEntity<ChannelDto> postPrivateChannel(@RequestBody PrivateChannelCreateRequest request){
        ChannelDto response = channelService.createPrivate(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 채널조회
    @RequestMapping(value ="/{channelId}", method = RequestMethod.GET)
    public ResponseEntity<ChannelDto> getChannel(@PathVariable UUID channelId){
        ChannelDto response = channelService.findChannel(channelId);

        return ResponseEntity.ok(response);
    }
    // 유저가 속한 채널 전체 조회
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ChannelDto>> getAllChannelByUserId(@RequestParam(required = true) UUID userId){
        if (userId == null) {
            return ResponseEntity.ok(channelService.findAllChannels());
        } else{
            return ResponseEntity.ok(channelService.findAllChannelsByUserId(userId));
        }


    }
    // 유저 참여
    @RequestMapping(value = "/{channelId}/join", method = RequestMethod.POST)
    public ResponseEntity<ChannelDto> joinUsers(@PathVariable("channelId") UUID id,
                                                @RequestBody ChannelJoinDto dto){
        ChannelDto response = channelService.joinUsers(id,
                dto.getUserList().toArray(new UUID[0]));
        return ResponseEntity.ok(response);
    }
    // 채널 업데이트
    @RequestMapping(value = "/{channelId}", method = RequestMethod.PATCH)
    public ResponseEntity<ChannelDto> updateChannel(@PathVariable("channelId") UUID id,
                                                      @RequestBody PublicChannelUpdateRequest dto){
        ChannelDto response = channelService.update(id,dto);
        return ResponseEntity.ok(response);
    }

    // 채널 삭제
    @RequestMapping(value = "/{channelId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteChannel(@PathVariable("channelId") UUID id){
        channelService.delete(id);
        return ResponseEntity.noContent().build(); // noContent는 객체를 만든다는 뜻, build를 붙혀서 객체 만듬

    }
}
