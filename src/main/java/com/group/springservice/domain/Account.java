package com.group.springservice.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter@Setter
@EqualsAndHashCode(of = "id")
@Builder@AllArgsConstructor@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue()
    private Long id;

    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String nickname;
    private String password;

    private boolean emailVerified;

    private String emailCheckToken;

    private LocalDateTime joinedAt;

    private String bio;

    private String url;

    private String occupation;

    private String location;

    @Lob@Basic(fetch = FetchType.EAGER)
    private String profileImage;

    private boolean studyCreatedByEmail;

    private boolean studyCreatedByWeb;

    private boolean studyEnrollmentResultByWeb;

    private boolean studyUpratedByEmail;

    private boolean studyUpratedByWeb;


    public void generateEmailToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
    }

    public void completeSignUp() {
        this.emailVerified = true;
        this.joinedAt = LocalDateTime.now();
    }
}
