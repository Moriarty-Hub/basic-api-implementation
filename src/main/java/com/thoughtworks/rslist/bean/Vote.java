package com.thoughtworks.rslist.bean;

import java.time.LocalDateTime;

public class Vote {
    private int userId;
    private int rsEventId;
    private int num;
    private LocalDateTime localDateTime;

    public Vote(int userId, int rsEventId, int num, LocalDateTime localDateTime) {
        this.userId = userId;
        this.rsEventId = rsEventId;
        this.num = num;
        this.localDateTime = localDateTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRsEventId() {
        return rsEventId;
    }

    public void setRsEventId(int rsEventId) {
        this.rsEventId = rsEventId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }
}
