package org.wongoo.wongoo3.global.policy.user;

public final class UserPolicy {
    private UserPolicy(){}
    // 길이 6~20, 영소문자 + 숫자
    public static final int USER_ID_MIN = 6;
    public static final int USER_ID_MAX = 20;
    public static final String USER_ID_REGEX = "^[a-z0-9]+$";

    // 길이 8~20, 영문자 + 숫자
    public static final int PASSWORD_MIN = 8;
    public static final int PASSWORD_MAX = 20;
    public static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+$";

    // 길이 5~50
    public static final int NICKNAME_MIN = 2;
    public static final int NICKNAME_MAX = 12;

    public static final int EMAIL_MAX = 50;

    public static final int INITIAL_POINT = 0;
}
