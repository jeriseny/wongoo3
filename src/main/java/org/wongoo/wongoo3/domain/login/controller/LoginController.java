package org.wongoo.wongoo3.domain.login.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wongoo.wongoo3.domain.login.dto.LoginRequest;
import org.wongoo.wongoo3.domain.login.service.LoginService;
import org.wongoo.wongoo3.domain.token.dto.TokenRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/login")
@Tag(name = "로그인", description = "로그인 관련 API")
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/local")
    public ResponseEntity<?> loginWithLocal(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(loginService.loginWithLocal(request.email(), request.password(), request.rememberMe()));
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissueTokens(@RequestBody TokenRequest request) {
        return ResponseEntity.ok(loginService.reissueTokens(request.token(), request.rememberMe()));
    }
}
