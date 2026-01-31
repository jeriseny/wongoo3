package org.wongoo.wongoo3.domain.user.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserInfoRequest {
    public String nickname;
    public String phoneNumber;
    public String profileImageUrl; //TODO 나중에 파일 업로드로 변경
}
