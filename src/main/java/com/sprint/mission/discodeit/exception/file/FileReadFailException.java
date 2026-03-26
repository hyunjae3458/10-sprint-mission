package com.sprint.mission.discodeit.exception.file;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class FileReadFailException extends FileException {
    public FileReadFailException(UUID binaryContentId) {
        super(ErrorCode.FILE_READ_FAIL, Map.of("binaryContentId", binaryContentId));
    }
}
