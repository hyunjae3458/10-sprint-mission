package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentMapper binaryContentMapper;

    @Override
    public BinaryContentDto create(BinaryContentCreateRequest dto) {
        BinaryContent binaryContent = new BinaryContent(null,
                null,
                dto.getSize(),
                dto.getFileData(),
                dto.getName(),
                dto.getContentType());

        binaryContentRepository.save(binaryContent);

        return binaryContentMapper.toDto(binaryContent);
    }
    @Override
    public BinaryContentDto findId(UUID id) {
        return  binaryContentMapper.toDto(getBinaryContentId(id));
    }

//    @Override
//    public BinaryContentDto findBinaryContentByUserId(UUID userId) {
//
//        return getBinaryContentByUserId(userId);
//    }

    @Override
    public List<BinaryContentDto> findAllIdIn(List<UUID> binaryContentId) {
        return binaryContentId.stream()
                .map(binaryContentRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(binaryContentMapper::toDto)
                .toList();
    }

//    @Override
//    public List<BinaryContentDto> findAllByMessageId(UUID messageId) {
//
//        return binaryContentRepository.findByMessageId(messageId);
//    }

    @Override
    public void delete(UUID id) {
        binaryContentRepository.delete(id);

    }

//    private BinaryContent getBinaryContentByUserId(UUID userId){
//        return binaryContentRepository.findByUserId(userId)
//                .orElseThrow(() -> new NoSuchElementException("해당 파일컨텐츠가 없습니다."));
//    }

    private BinaryContent getBinaryContentId(UUID id){
        return binaryContentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 파일컨텐츠가 없습니다."));
    }
}
