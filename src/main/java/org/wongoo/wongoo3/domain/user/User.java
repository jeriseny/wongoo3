package org.wongoo.wongoo3.domain.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.wongoo.wongoo3.domain.auth.dto.SocialType;
import org.wongoo.wongoo3.domain.auth.dto.UserRole;
import org.wongoo.wongoo3.domain.user.dto.UserStatus;
import org.wongoo.wongoo3.global.jpa.BaseTimeEntity;

import java.time.LocalDateTime;

/**
 * 유저 엔티티
 * 일반 회원과 소셜 회원을 모두 관리
 * 일반 회원 : email, password, nickname, phoneNumber, status, profileImageUrl
 * 소셜 회원 : email,           nickname, phoneNumber, status, profileImageUrl, socialType, socialId,
 * UNIQUE 제약조건 : socialType + socialId / email / phoneNumber / nickname
 *
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "WK_USER")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatus status;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @Column(name = "password")
    private String password;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "social_type")
    private SocialType socialType;

    @Column(name = "social_id")
    private String socialId;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    public static User createLocalUser(String email, String nickname, String phoneNumber, String password) {
        User user = new User();
        user.email = email;
        user.nickname = nickname;
        user.phoneNumber = phoneNumber;
        user.password = password;
        user.status = UserStatus.ACTIVE;
        user.role = UserRole.USER;
        return user;
    }

    public static User createSocialUser(String email, String nickname, String phoneNumber, SocialType socialType, String socialId) {
        User user = new User();
        user.email = email;
        user.nickname = nickname;
        user.phoneNumber = phoneNumber;
        user.socialType = socialType;
        user.socialId = socialId;
        user.status = UserStatus.ACTIVE;
        user.role = UserRole.USER;
        return user;
    }

    public void changeRole(UserRole role) {
        this.role = role;
    }
}
