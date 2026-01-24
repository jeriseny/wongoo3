package org.wongoo.wongoo3.domain.user.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.wongoo.wongoo3.domain.auth.dto.UserRole;
import org.wongoo.wongoo3.domain.user.dto.LocalSignUpRequest;
import org.wongoo.wongoo3.domain.user.dto.LoginUser;
import org.wongoo.wongoo3.domain.user.service.UserService;
import org.wongoo.wongoo3.global.jwt.annotation.CurrentUser;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "회원", description = "회원 관련 API")
public class UserController {

    private final UserService userService;


    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/admin/role/{userId}")
    public ResponseEntity<?> changeUserRole(@CurrentUser LoginUser loginUser, @PathVariable Long userId, @RequestParam UserRole role) {
        userService.changeUserRole(userId, role, loginUser.userId());
        return ResponseEntity.ok("유저 권한이 변경되었습니다");
    }


    @PostMapping("/signup")
    public ResponseEntity<Long> signUp(@Valid @RequestBody LocalSignUpRequest request) {
        return ResponseEntity.ok(userService.signUp(request));
    }

}
