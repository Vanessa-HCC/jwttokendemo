package com.example.jwttokendemo.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private int userId;

    @Column(name = "username", nullable = false, unique = true, length = 256)
    private String username;
    
    @Column(name = "nickname", nullable = false, length = 20)
    private String nickname;

    @Column(name = "password", length = 18)
    private String password;

    @Column(name = "createdDate")
    private LocalDateTime createdDate;

    @PrePersist
    public void prePersist() {
        if(createdDate == null){
            createdDate = LocalDateTime.now();
        }
    }

}
