package org.wongoo.wongoo3.domain.auth.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wongoo.wongoo3.domain.auth.dto.LoginRequest;
import org.wongoo.wongoo3.domain.auth.oauth2.dto.OAuth2UserProfile;
import org.wongoo.wongoo3.domain.auth.oauth2.dto.ProviderType;
import org.wongoo.wongoo3.domain.auth.oauth2.service.OAuth2Service;
import org.wongoo.wongoo3.domain.auth.service.AuthService;
import org.wongoo.wongoo3.domain.token.dto.TokenRequest;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "로그인", description = "로그인 관련 API")
public class AuthController {

    private final AuthService authService;
    private final OAuth2Service oAuth2Service;

    @PostMapping("/login/local")
    public ResponseEntity<?> loginWithLocal(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authService.login(request.email(), request.password(), request.rememberMe()));
    }

    @Deprecated(since = "OAuth2 로그인으로 대체됨", forRemoval = true)
    @PostMapping("/login/oauth2")
    public ResponseEntity<?> loginWithOAuth2(
            @RequestParam ProviderType providerType,
            @RequestParam String code,
            @RequestParam String state,
            @RequestParam(defaultValue = "false") boolean rememberMe) {
        OAuth2UserProfile userProfile = oAuth2Service.getUserProfile(providerType, code, state);

        return ResponseEntity.ok(oAuth2Service.processOAuth2Login(userProfile, providerType, rememberMe));
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissueTokens(@RequestBody TokenRequest request) {
        return ResponseEntity.ok(authService.reissueTokens(request.token(), request.rememberMe()));
    }

    @Deprecated(since = "OAuth2 로그인으로 대체됨", forRemoval = true)
    @GetMapping("/oauth2/uri")
    public ResponseEntity<?> getLoginPage(@RequestParam ProviderType providerType) {
        String state = java.util.UUID.randomUUID().toString();
        return ResponseEntity.ok(oAuth2Service.getLoginUri(providerType, state));
    }

//    @PostMapping("/logout")


    // 네이버 콜백 테스트
    @GetMapping("/naver/callback")
    public ResponseEntity<?> naverCallback(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String error,
            @RequestParam(required = false, name = "error_description") String errorDescription
    ) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", code);
        response.put("state", state);
        response.put("error", error);
        response.put("error_description", errorDescription);

        return ResponseEntity.ok(response);
    }

    // 카카오 콜백 테스트
    // ⚠️ 카카오는 배포 후 개발자 콘솔에 서버 IP 재등록 필요
    @GetMapping("/kakao/callback")
    public ResponseEntity<?> kakaoCallback(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String error,
            @RequestParam(required = false, name = "error_description") String errorDescription
    ) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", code);
        response.put("state", state);
        response.put("error", error);
        response.put("error_description", errorDescription);

        return ResponseEntity.ok(response);
    }
}
