package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User extends BaseUpdatableEntity{
    @Column(name="user_name", unique = true, nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id")
    private BinaryContent profileImg;

    @Column(name = "password", nullable = false)
    private String password;

    public User(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void addProfileImage(BinaryContent profileImg){
        this.profileImg = profileImg;
    }

    public void updateName(String name){this.name = name;}

    public void updateEmail(String email){
        this.email = email;
    }

    public void updateProfileImg(BinaryContent profileImg){this.profileImg = profileImg;}

    public void updatePassword(String password){this.password = password;}

    @Override
    public String toString() {
        return name;
    }
}
