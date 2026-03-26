package com.sprint.mission.discodeit.exception.others;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public class FileUploadFailException extends DiscodeitException {
    public FileUploadFailException() {
        super(ErrorCode.FILE_UPLOAD_FAIL, Map.of());
    }
}
