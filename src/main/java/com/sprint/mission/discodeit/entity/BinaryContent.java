package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "binary_contents")
@NoArgsConstructor
public class BinaryContent extends BaseEntity{

    @Column(name = "size")
    private long size;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "content_type")
    private String contentType;

    public BinaryContent(long size, String fileName, String contentType){
        this.size = size;
        this.fileName = fileName;
        this.contentType = contentType;
    }
}
