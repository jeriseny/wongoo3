package org.wongoo.wongoo3.domain.terms.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.wongoo.wongoo3.domain.terms.Terms;
import org.wongoo.wongoo3.domain.terms.dto.AddTermsRequest;
import org.wongoo.wongoo3.domain.terms.dto.TermsType;
import org.wongoo.wongoo3.domain.terms.history.TermsHistory;
import org.wongoo.wongoo3.domain.terms.history.repository.TermsHistoryRepository;
import org.wongoo.wongoo3.domain.terms.repository.TermsRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@SpringBootTest
@ActiveProfiles("test")
class TermsServiceTest {

    List<AddTermsRequest> requests;
    List<AddTermsRequest> updateRequests;

    @Autowired
    TermsService termsService;
    @Autowired
    TermsRepository termsRepository;
    @Autowired
    TermsHistoryRepository termsHistoryRepository;

    @BeforeEach
    void setUp() {
        requests = List.of(
                new AddTermsRequest(TermsType.SERVICE, "서비스 약관 내용", true, true),
                new AddTermsRequest(TermsType.PRIVACY, "개인정보 처리방침 내용", true, true),
                new AddTermsRequest(TermsType.MARKETING, "마케팅 활용 동의 내용", false, true),
                new AddTermsRequest(TermsType.KAKAO_AD, "카카오 광고 동의 내용", false, true)
        );
        updateRequests = List.of(
                new AddTermsRequest(TermsType.SERVICE, "서비스 약관 내용", true, true),
                new AddTermsRequest(TermsType.PRIVACY, "개인정보 처리방침 내용", true, true),
                new AddTermsRequest(TermsType.MARKETING, "마케팅 활용 동의 내용 - 수정", false, true),
                new AddTermsRequest(TermsType.KAKAO_AD, "카카오 광고 동의 내용 - 수정", false, true)
        );
    }

    @Test
    @DisplayName("관리자 약관 등록 테스트")
    @Transactional
    void addTerms() {
        // When
        termsService.addTerms(requests);

        // Then
        List<Terms> termsList = termsRepository.findAll();
        assertThat(termsList).hasSize(4);

        for (AddTermsRequest req : requests) {
            Terms terms = termsList.stream()
                    .filter(t -> t.getType() == req.termsType())
                    .findFirst()
                    .orElseThrow();

            assertThat(terms.getContent()).isEqualTo(req.content());
            assertThat(terms.isRequired()).isEqualTo(req.required());
            assertThat(terms.isActive()).isEqualTo(req.isActive());
            assertThat(terms.getTermsVersion()).isEqualTo(0L);
        }
    }

    @Test
    @DisplayName("관리자 약관 수정 테스트 - 내용 변경 시 버전 증가")
    @Transactional
    void updateTerms() {
        // given
        termsService.addTerms(requests);

        // when
        termsService.addTerms(updateRequests);

        // then
        List<Terms> termsList = termsRepository.findAll();
        assertThat(termsList).hasSize(4);

        for (AddTermsRequest req : updateRequests) {
            Terms terms = termsList.stream()
                    .filter(t -> t.getType() == req.termsType())
                    .findFirst()
                    .orElseThrow();

            assertThat(terms.getContent()).isEqualTo(req.content());

            if (req.content().contains("수정")) {
                assertThat(terms.getTermsVersion()).isEqualTo(1L);
            } else {
                assertThat(terms.getTermsVersion()).isEqualTo(0L);
            }
        }
    }

    @Test
    @DisplayName("약관 수정 시 히스토리에 수정 전 버전 저장")
    @Transactional
    void updateTerms_history_saved() {

        // given
        termsService.addTerms(requests);

        // 수정 전 상태 스냅샷
        Terms beforeMarketing = termsRepository.findByType(TermsType.MARKETING).orElseThrow();
        String beforeContent = beforeMarketing.getContent();
        Long beforeVersion = beforeMarketing.getTermsVersion();

        // when
        termsService.addTerms(updateRequests);

        // then
        List<TermsHistory> histories = termsHistoryRepository.findAll();

        // MARKETING, KAKAO_AD 두 개만 변경됨
        assertThat(histories).hasSize(2);

        TermsHistory marketingHistory = histories.stream()
                .filter(h -> h.getType() == TermsType.MARKETING)
                .findFirst()
                .orElseThrow();

        assertThat(marketingHistory.getContent()).isEqualTo(beforeContent);
        assertThat(marketingHistory.getTermsVersion()).isEqualTo(beforeVersion);
    }

}