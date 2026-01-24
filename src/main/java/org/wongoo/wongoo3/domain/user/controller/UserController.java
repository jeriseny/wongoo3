package org.wongoo.wongoo3.domain.user.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wongoo.wongoo3.domain.user.dto.LocalSignUpRequest;
import org.wongoo.wongoo3.domain.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "회원", description = "회원 관련 API")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Long> signUp(@Valid @RequestBody LocalSignUpRequest request) {
        return ResponseEntity.ok(userService.signUp(request));
    }

}
