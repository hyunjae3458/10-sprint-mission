package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@Tag(name = "Message", description = "Message API")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    // 메시지 생성
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageDto> postMessage(@RequestPart("messageCreateRequest") MessageCreateRequest request,
                                                  @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments){
        MessageDto response = messageService.create(request, attachments);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 메시지 단건 조회
//    @RequestMapping(value = "/message/{id}", method = RequestMethod.GET)
//    public ResponseEntity<MessageDto> getMessage(@PathVariable UUID id){
//        MessageDto response = messageService.findMessage(id);
//        return ResponseEntity.ok(response);
//    }

    // 채널 내 메시지 조회(키워드 따라 조회 가능)
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<MessageDto>> getAllMessage(@RequestParam(required = true) UUID channelId,
                                                          @RequestParam(required = false) String keyword){
        if(keyword == null){
            return ResponseEntity.ok(messageService.findAllMessagesByChannelId(channelId));
        } else{
            return ResponseEntity.ok(messageService.findMessageByKeyword(channelId,keyword));
        }

    }

    // 메시지 업데이트
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<MessageDto> updateMessage(@PathVariable UUID id,
                                                    @RequestBody MessageUpdateRequest dto){
        MessageDto response = messageService.update(id,dto);
        return ResponseEntity.ok(response);
    }

    // 메시지 삭제
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID id){
        messageService.delete(id);
        return ResponseEntity.noContent().build(); // noContent는 객체를 만든다는 뜻, build를 붙혀서 객체 만듬

    }
}
