package com.thoughtworks.rslist.bean;

import javax.validation.constraints.NotNull;

public class RsEvent {
    @NotNull(message = "invalid param")
    private String name;
    @NotNull(message = "invalid param")
    private String keyword;
    @NotNull(message = "invalid param")
    private User user;

    public RsEvent(String name, String keyword, User user) {
        this.name = name;
        this.keyword = keyword;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
