package com.will.common;

public enum ResultCodeEnum {
    SUCCESS(200,"success"),
    PARAMS_ERROR(501,"paramsError"),
    USERNAME_ERROR(502,"usernameError"),
    PASSWORD_ERROR(503,"passwordError"),
    CHECK_PASSWORD_ERROR(504,"checkPasswordError"),
    NOTLOGIN(505,"notLogin"),
    USERNAME_USED(506,"userNameUsed"),
    DATABASE_ERROR(507,"DataBaseError"),
    SYSTEM_ERROR(508,"systemError"),
    OPERATION_ERROR(509,"operationError");

    private Integer code;
    private String message;
    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    public Integer getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
}
