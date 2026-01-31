package org.wongoo.wongoo3.domain.terms.hisotry.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wongoo.wongoo3.domain.terms.Terms;
import org.wongoo.wongoo3.domain.terms.hisotry.TermsHistory;
import org.wongoo.wongoo3.domain.terms.hisotry.repository.TermsHistoryRepository;

@Service
@RequiredArgsConstructor
public class TermsHistoryService {

    private final TermsHistoryRepository termsHistoryRepository;

    public void saveTermsHistory(Terms terms) {
        TermsHistory from = TermsHistory.from(terms);
        termsHistoryRepository.save(from);
    }
}
