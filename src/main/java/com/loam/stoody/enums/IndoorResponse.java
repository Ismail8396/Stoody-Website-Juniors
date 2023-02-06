package com.loam.stoody.enums;

public enum IndoorResponse {
    INIT,
    SUCCESS,
    FAIL,
    ACCESS_DENIED,// When unauthorized request is made
    BAD_REQUEST,// When request does not carry acceptable data
    TOKEN_EXPIRED,
    TOKEN_ABSENT,
    USERNAME_EXIST,
    EMAIL_EXIST,
    USERNAME_EMAIL_EXIST,
    OTP_REQUIRED_AND_SENT,
    ACCOUNT_TEMPORAL_BLOCK_TOO_MANY_REQUESTS
}
