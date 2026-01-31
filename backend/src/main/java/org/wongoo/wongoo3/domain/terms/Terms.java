package org.wongoo.wongoo3.domain.terms;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.wongoo.wongoo3.domain.terms.dto.TermsType;
import org.wongoo.wongoo3.global.jpa.BaseTimeEntity;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "WK_TERMS")
public class Terms extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TermsType type;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "required", nullable = false)
    private boolean required;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "terms_version", nullable = false)
    private Long termsVersion;

    public Terms(TermsType type, String content, boolean required, boolean isActive) {
        this.type = type;
        this.content = content;
        this.required = required;
        this.isActive = isActive;
        this.termsVersion = 0L;
    }

    public void update(String content, boolean required, boolean isActive) {
        this.content = content;
        this.required = required;
        this.isActive = isActive;
        this.termsVersion++;
    }
}
