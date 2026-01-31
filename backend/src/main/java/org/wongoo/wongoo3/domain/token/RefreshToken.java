package org.wongoo.wongoo3.domain.token;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.wongoo.wongoo3.domain.user.User;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "WK_REFRESH_TOKEN")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "token", length = 500, nullable = false)
    private String token;

    @Column(name = "expiration", nullable = false)
    private LocalDateTime expiration;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public RefreshToken(User user, String token, LocalDateTime expiration) {
        this.user = user;
        this.expiration = expiration;
        this.token = token;
    }
}
