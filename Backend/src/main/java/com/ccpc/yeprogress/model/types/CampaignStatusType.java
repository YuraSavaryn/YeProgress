package com.ccpc.yeprogress.model.types;

public enum CampaignStatusType {
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed");
    private final String displayName;

    CampaignStatusType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static CampaignStatusType getDefault() {
        return IN_PROGRESS;
    }
}