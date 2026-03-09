package com.sprint.mission.discodeit.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PageResponse<T> {
    List<T> content;
    Object nextCursor;
    int number;
    int size;
    boolean hasNext;
    Long TotalElements;
}
