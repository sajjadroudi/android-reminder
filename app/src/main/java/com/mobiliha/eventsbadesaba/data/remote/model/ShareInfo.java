package com.mobiliha.eventsbadesaba.data.remote.model;

public class ShareInfo {
    private final String link;
    private final String baseId;
    private final String token;

    public ShareInfo(String link, String baseId, String token) {
        this.link = link;
        this.baseId = baseId;
        this.token = token;
    }

    public String getLink() {
        return link;
    }

    public String getBaseId() {
        return baseId;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "ShareInfo{" +
                "link='" + link + '\'' +
                ", baseId='" + baseId + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
