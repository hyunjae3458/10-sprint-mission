package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
@Table(name = "binary_contents")
@NoArgsConstructor
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private UUID id;

    @Column(name = "size")
    private long size;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "bytes")
    private byte[] bytes;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "content_type")
    private String contentType;

    public BinaryContent(long size, byte[] bytes, String fileName, String contentType){
        this.id = UUID.randomUUID();
        this.size = size;
        this.createdAt = Instant.now();
        this.bytes = bytes;
        this.fileName = fileName;
        this.contentType = contentType;
    }
}
