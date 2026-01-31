package org.wongoo.wongoo3.domain.terms.userterms;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.wongoo.wongoo3.domain.terms.Terms;
import org.wongoo.wongoo3.domain.user.User;
import org.wongoo.wongoo3.global.jpa.BaseTimeEntity;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "WK_USER_TERMS")
public class UserTerms extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terms_id", nullable = false)
    private Terms terms;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "agreed", nullable = false)
    private boolean agreed;

    @Column(name = "agreed_terms_version", nullable = false)
    private Long agreedTermsVersion;

    @Column(name = "agreed_at")
    private LocalDateTime agreedAt;

    public UserTerms(Terms terms, User user, boolean agreed, Long agreedTermsVersion) {
        this.terms = terms;
        this.user = user;
        this.agreed = agreed;
        this.agreedTermsVersion = agreedTermsVersion;
        if (agreed) {
            this.agreedAt = LocalDateTime.now();
        }
    }
}
