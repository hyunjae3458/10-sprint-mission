package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User extends BaseUpdatableEntity{
    @Column(name="username", unique = true, nullable = false)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id")
    private BinaryContent profile;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserStatus userStatus;

    public User(String name, String email, String password){
        this.username = name;
        this.email = email;
        this.password = password;
    }

    public void addProfileImage(BinaryContent profileImg){
        this.profile = profileImg;
    }

    public void updateName(String name){this.username = name;}

    public void updateEmail(String email){
        this.email = email;
    }

    public void updateProfileImg(BinaryContent profileImg){this.profile = profileImg;}

    public void updatePassword(String password){this.password = password;}

    @Override
    public String toString() {
        return username;
    }
}
