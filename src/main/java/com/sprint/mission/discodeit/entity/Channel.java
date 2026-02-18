package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.type.ChannelType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Channel extends BaseEntity {
    private String channelName;
    private List<UUID> userList;
    private List<UUID> messageList;
    private ChannelType channelType;
    private String description;

    public Channel(String channelName, String description) {
        this.channelName = channelName;
        this.description = description;
        this.userList = new ArrayList<>();
        this.messageList = new ArrayList<>();
    }

    public Channel(){
        this.userList = new ArrayList<>();
        this.messageList = new ArrayList<>();
    }

    public void addUsers(UUID userId){
        if(!this.getUserList().contains(userId)){
            this.getUserList().add(userId);
        }
    }

    public void addMessage(UUID messageId){
        if(!this.getMessageList().contains(messageId)){
            this.getMessageList().add(messageId);
        }
    }

    public void updateChannel(String newChannelName,String newDescriptionn){
        this.channelName = newChannelName;
        this.description = newDescriptionn;
        updateTimeStamp();
    }

    @Override
    public String toString() {
        return this.getChannelName();
    }
}
