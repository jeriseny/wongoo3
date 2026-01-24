package org.wongoo.wongoo3.domain.terms.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wongoo.wongoo3.domain.terms.dto.AddTermsRequest;
import org.wongoo.wongoo3.domain.terms.service.TermsService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/terms")
@Tag(name = "약관", description = "약관 관련 API")
public class TermsController {

    private final TermsService termsService;

    /**
     * 관리자 약관 목록 조회
     */
    // TODO : 관리자 권한 인증 추가
    @GetMapping("/admin/list")
    public ResponseEntity<?> getTermsList() {
        return ResponseEntity.ok(termsService.getAllTerms());
    }

    /**
     * 관리자 약관 등록 및 수정
     */
    // TODO : 관리자 권한 인증 추가
    @PostMapping("admin/upsert")
    public ResponseEntity<?> upsertTerms(@RequestBody @Valid List<AddTermsRequest> requests) {
        termsService.addTerms(requests);
        return ResponseEntity.ok("약관이 성공적으로 등록/수정되었습니다.");
    }

}
