package com.ccpc.yeprogress.model.types;

public enum AuthenticationStatusType {
    NOT_VERIFIED("Not Verified"),
    VERIFIED("Verified");

    private final String displayName;

    AuthenticationStatusType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static AuthenticationStatusType getDefault() {
        return NOT_VERIFIED;
    }
}