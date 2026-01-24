package org.wongoo.wongoo3.domain.terms.userterms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wongoo.wongoo3.domain.terms.Terms;
import org.wongoo.wongoo3.domain.terms.dto.TermsRequest;
import org.wongoo.wongoo3.domain.terms.dto.TermsType;
import org.wongoo.wongoo3.domain.terms.service.TermsService;
import org.wongoo.wongoo3.domain.terms.userterms.UserTerms;
import org.wongoo.wongoo3.domain.terms.userterms.repository.UserTermsRepository;
import org.wongoo.wongoo3.domain.user.User;
import org.wongoo.wongoo3.global.exception.WebErrorCode;
import org.wongoo.wongoo3.global.exception.WebErrorException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserTermsService {

    private final TermsService termsService;
    private final UserTermsRepository userTermsRepository;

    public List<UserTerms> getUserTermsByUser(User user) {
        return userTermsRepository.findByUser(user);
    }

    public void addUserTerms(User user, List<TermsRequest> termsRequests) {
        List<Terms> allTerms = termsService.getAllTerms();

        if (allTerms.size() != termsRequests.size()) {
            throw new WebErrorException(WebErrorCode.BAD_REQUEST, "약관 동의 개수가 일치하지 않습니다.");
        }

        Map<TermsType, Terms> termsMap = allTerms.stream().collect(Collectors.toMap(Terms::getType, terms -> terms));

        for (TermsRequest request : termsRequests) {
            Terms terms = termsMap.get(request.termsType());
            if (terms == null) {
                throw new WebErrorException(WebErrorCode.BAD_REQUEST, "존재하지 않는 약관 유형입니다: " + request.termsType());
            }
            if (!request.agreed() && terms.isRequired()) {
                throw new WebErrorException(WebErrorCode.BAD_REQUEST, "필수 약관에 동의하지 않았습니다: " + request.termsType());
            }
            UserTerms userTerms = new UserTerms(terms, user, request.agreed(), terms.getTermsVersion());
            userTermsRepository.save(userTerms);
        }

    }
}
