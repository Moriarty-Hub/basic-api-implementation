package com.thoughtworks.rslist.dto;

import javax.persistence.*;

@Entity
@Table(name = "rs_event")
public class RsEventDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String keyword;
    private int voteNum;

    @ManyToOne
    private UserDto userDto;

    public RsEventDto() {

    }

    public RsEventDto(String name, String keyword, UserDto userDto) {
        this.name = name;
        this.keyword = keyword;
        this.userDto = userDto;
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

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserId(UserDto userDto) {
        this.userDto = userDto;
    }

    public int getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(int voteNum) {
        this.voteNum = voteNum;
    }

    @Override
    public String toString() {
        return "RsEventDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", keyword='" + keyword + '\'' +
                ", userDto=" + userDto.toString() +
                '}';
    }

}
