package com.sprint.mission.discodeit.dto.page;

import lombok.Getter;

import java.awt.print.Pageable;
import java.util.List;

@Getter
public class PageResponse {
    private List<Pageable> content;
    private int number;
    private int size;
    private boolean hasNext;
    private long totalElements;

}
