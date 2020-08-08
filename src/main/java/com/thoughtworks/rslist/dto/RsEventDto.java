package com.thoughtworks.rslist.dto;

import javax.persistence.*;

@Entity
@Table(name = "rs_event")
public class RsEventDto {
    @Id
    @GeneratedValue()
    private int id;
    private String name;
    private String keyword;
    private int userId;

    public RsEventDto() {

    }

    public RsEventDto(String name, String keyword, int userId) {
        this.name = name;
        this.keyword = keyword;
        this.userId = userId;
    }

    public int getId() {
        return id;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "RsEventDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", keyword='" + keyword + '\'' +
                ", userId=" + userId +
                '}';
    }

    @ManyToOne
    private UserDto userDtoList;
}
