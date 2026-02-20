package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContents")
@Tag(name = "BinaryContents", description = "BinaryContent API")
@RequiredArgsConstructor
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    // 단건 조회
    @RequestMapping(value = "/{binaryContentId}", method = RequestMethod.GET)
    public ResponseEntity<BinaryContentDto> getBinaryContent(@PathVariable("binaryContentId") UUID id){
        BinaryContentDto response = binaryContentService.findId(id);
        return ResponseEntity.ok(response);
    }

    // 다수 조회
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<BinaryContentDto>> getAllBinaryContent(@RequestParam("binaryContentIds") List<UUID> binaryContentIds){
        List<BinaryContentDto> responseList = binaryContentService.findAllIdIn(binaryContentIds);
        return ResponseEntity.ok(responseList);
    }

//    // 프로필 조회
//    @RequestMapping(value = "/user/{user-id}", method = RequestMethod.GET)
//    public ResponseEntity<BinaryContent> getProfileImg(@PathVariable("user-id") UUID userId){
//        BinaryContent response = binaryContentService.findBinaryContentByUserId(userId);
//        return ResponseEntity.ok(response);
//    }
//
//    // 메시지 첨부파일 조회
//    @RequestMapping(value = "/message/{message-id}", method = RequestMethod.GET)
//    public ResponseEntity<List<BinaryContent>> getAllMessageBinaryContent(@PathVariable("message-id") UUID messageId){
//        List<BinaryContent> responseList = binaryContentService.findAllByMessageId(messageId);
//        return ResponseEntity.ok(responseList);
//    }
}
