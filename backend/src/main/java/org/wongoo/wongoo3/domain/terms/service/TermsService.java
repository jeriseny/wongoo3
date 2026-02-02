package org.wongoo.wongoo3.domain.terms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wongoo.wongoo3.domain.terms.Terms;
import org.wongoo.wongoo3.domain.terms.dto.AddTermsRequest;
import org.wongoo.wongoo3.domain.terms.dto.TermsType;
import org.wongoo.wongoo3.domain.terms.history.service.TermsHistoryService;
import org.wongoo.wongoo3.domain.terms.repository.TermsRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TermsService {

    private final TermsHistoryService termsHistoryService;
    private final TermsRepository termsRepository;

    @Transactional(readOnly = true)
    public List<Terms> getAllTerms() {
        return termsRepository.findAll();
    }

    /**
     * 관리자 약관 등록/수정
     * @param requests 약관 등록/수정 요청 리스트
     */
    @Transactional
    public void addTerms(List<AddTermsRequest> requests) {
        List<Terms> termsList = termsRepository.findAll();
        Map<TermsType, Terms> termsMap = termsList.stream().collect(Collectors.toMap(Terms::getType, terms -> terms));

        for (AddTermsRequest request : requests) {
            Terms terms = termsMap.get(request.termsType());
            if (terms == null) {
                Terms newTerms = new Terms(request.termsType(), request.content(), request.required(), request.isActive());
                termsRepository.save(newTerms);
            } else if (!terms.getContent().equals(request.content())) {
                termsHistoryService.saveTermsHistory(terms);
                terms.update(request.content(), request.required(), request.isActive());
            }
        }
    }
}
