package org.wongoo.wongoo3.domain.terms.hisotry;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.wongoo.wongoo3.domain.terms.Terms;
import org.wongoo.wongoo3.domain.terms.dto.TermsType;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "WK_TERMS_HISTORY")
public class TermsHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terms_id", nullable = false)
    private Terms terms;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TermsType type;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "required", nullable = false)
    private boolean required;

    @Column(name = "terms_version", nullable = false)
    private Long termsVersion;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    LocalDateTime createdAt;

    public static TermsHistory from(Terms terms) {
        TermsHistory termsHistory = new TermsHistory();
        termsHistory.terms = terms;
        termsHistory.type = terms.getType();
        termsHistory.content = terms.getContent();
        termsHistory.required = terms.isRequired();
        termsHistory.termsVersion = terms.getTermsVersion();
        return termsHistory;
    }
}
