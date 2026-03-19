package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContents")
@Tag(name = "BinaryContents", description = "파일 관리 관련 API")
@RequiredArgsConstructor
public class BinaryContentController {
    private final BinaryContentService binaryContentService;
    private final BinaryContentStorage binaryContentStorage;

    // 단건 조회
    @RequestMapping(value = "/{binaryContentId}", method = RequestMethod.GET)
    public ResponseEntity<BinaryContentDto> getBinaryContent(@PathVariable("binaryContentId") UUID id){
        BinaryContentDto response = binaryContentService.findBinaryContent(id);
        return ResponseEntity.ok(response);
    }

    // 다수 조회
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<BinaryContentDto>> getAllBinaryContent(@RequestParam("binaryContentIds") List<UUID> binaryContentIds){
        List<BinaryContentDto> responseList = binaryContentService.findAllIdIn(binaryContentIds);
        return ResponseEntity.ok(responseList);
    }

    @RequestMapping(value = "/{binaryContentId}/download", method = RequestMethod.GET)
    public ResponseEntity<?> download(@PathVariable("binaryContentId") UUID id){

        return binaryContentStorage.download(binaryContentService.findBinaryContent(id));
    }
}
