package com.sprint.mission.discodeit.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "messages")
public class Message extends BaseUpdatableEntity{
    @Column(name = "content", columnDefinition = "TEXT")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    // cascade로 전체가 같이 처리되도록하고 orphanRemoval로 만약 리스트에서 객체가 빠진다면 해당 데이터를 해당 테이블에서 지움
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name = "message_attachments",
            joinColumns = @JoinColumn(name = "message_id"),
            inverseJoinColumns = @JoinColumn(name = "binary_content_id")
    )
    private List<BinaryContent> attachments = new ArrayList<>();

    public Message(User author, String text, Channel channel) {
        this.author = author;
        this.text = text;
        this.channel = channel;
    }

    public void updateMessage(String newText){
        this.text = newText;
    }

    public void addAttachment(BinaryContent attachment){
        attachments.add(attachment);
    }

    @Override
    public String toString() {
        return this.getText();
    }
}
