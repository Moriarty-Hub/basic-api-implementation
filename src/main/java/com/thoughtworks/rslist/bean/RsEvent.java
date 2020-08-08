package com.thoughtworks.rslist.bean;

import javax.validation.constraints.NotNull;

public class RsEvent {
    @NotNull(message = "invalid param")
    private String name;
    @NotNull(message = "invalid param")
    private String keyword;
    @NotNull(message = "invalid param")
    private Integer userId;

    public RsEvent(String name, String keyword, Integer userId) {
        this.name = name;
        this.keyword = keyword;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "RsEvent{" +
                "name='" + name + '\'' +
                ", keyword='" + keyword + '\'' +
                ", userId=" + userId +
                '}';
    }
}
