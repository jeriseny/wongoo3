package org.wongoo.wongoo3.domain.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.wongoo.wongoo3.domain.auth.dto.UserRole;
import org.wongoo.wongoo3.domain.auth.oauth2.dto.ProviderType;
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
    @Column(name = "provider_type")
    private ProviderType providerType;

    @Column(name = "provider_id")
    private String providerId;

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

    public static User createSocialUser(String email, String nickname, String phoneNumber, ProviderType providerType, String providerId) {
        User user = new User();
        user.email = email;
        user.nickname = nickname;
        user.phoneNumber = phoneNumber;
        user.providerType = providerType;
        user.providerId = providerId;
        user.status = UserStatus.ACTIVE;
        user.role = UserRole.USER;
        return user;
    }

    public void changeRole(UserRole role) {
        this.role = role;
    }
    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void changeInfo(String nickname, String phoneNumber) {
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
    }
}
