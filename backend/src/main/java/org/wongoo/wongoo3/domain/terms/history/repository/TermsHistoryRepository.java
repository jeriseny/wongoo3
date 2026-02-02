package org.wongoo.wongoo3.domain.terms.history.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wongoo.wongoo3.domain.terms.history.TermsHistory;

public interface TermsHistoryRepository extends JpaRepository<TermsHistory, Long> {
}
