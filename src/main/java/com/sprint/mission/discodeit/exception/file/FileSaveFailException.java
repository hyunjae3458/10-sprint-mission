package com.sprint.mission.discodeit.exception.file;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class FileSaveFailException extends FileException {
    public FileSaveFailException(UUID binaryContentId) {
        super(ErrorCode.FILE_SAVE_FAIL, Map.of("binaryContentId", binaryContentId));
    }
}
