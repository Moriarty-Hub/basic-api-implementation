package com.thoughtworks.rslist.bean;

import javax.validation.constraints.NotNull;

public class RsEvent {
    @NotNull(message = "invalid param")
    private String name;
    @NotNull(message = "invalid param")
    private String keyword;
    @NotNull(message = "invalid param")
    private Integer userId;
    private Integer voteNum;

    public RsEvent(String name, String keyword, Integer userId) {
        this.name = name;
        this.keyword = keyword;
        this.userId = userId;
        voteNum = 0;
    }

    public RsEvent(String name, String keyword, Integer userId, Integer voteNum) {
        this.name = name;
        this.keyword = keyword;
        this.userId = userId;
        this.voteNum =voteNum;
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

    public Integer getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(Integer voteNum) {
        this.voteNum = voteNum;
    }

    @Override
    public String toString() {
        return "RsEvent{" +
                "name='" + name + '\'' +
                ", keyword='" + keyword + '\'' +
                ", userId=" + userId +
                ", voteNum=" + voteNum +
                '}';
    }
}
