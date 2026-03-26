package com.sprint.mission.discodeit.exception.file;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public class FileUploadFailException extends FileException {
    public FileUploadFailException() {
        super(ErrorCode.FILE_UPLOAD_FAIL, Map.of());
    }
}
