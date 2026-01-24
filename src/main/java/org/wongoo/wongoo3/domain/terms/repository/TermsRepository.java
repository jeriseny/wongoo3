package org.wongoo.wongoo3.domain.terms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wongoo.wongoo3.domain.terms.Terms;
import org.wongoo.wongoo3.domain.terms.dto.TermsType;

import java.util.Optional;

public interface TermsRepository extends JpaRepository<Terms, Long> {
    public Optional<Terms> findByType(TermsType type);
}
