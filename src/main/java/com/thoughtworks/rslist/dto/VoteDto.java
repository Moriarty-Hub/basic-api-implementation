package com.thoughtworks.rslist.dto;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vote")
public class VoteDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDateTime localDateTime;
    private int num;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private RsEventDto rsEventDto;
    @ManyToOne
    @JoinColumn(name = "rs_event_id")
    private UserDto userDto;

    public VoteDto(LocalDateTime localDateTime, int num, RsEventDto rsEventDto, UserDto userDto) {
        this.localDateTime = localDateTime;
        this.num = num;
        this.rsEventDto = rsEventDto;
        this.userDto = userDto;
    }

    public VoteDto() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public RsEventDto getRsEventDto() {
        return rsEventDto;
    }

    public void setRsEventDto(RsEventDto rsEventDto) {
        this.rsEventDto = rsEventDto;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    @Override
    public String toString() {
        return "VoteDto{" +
                "id=" + id +
                ", localDateTime=" + localDateTime +
                ", num=" + num +
                ", rsEventDto=" + rsEventDto +
                ", userDto=" + userDto +
                '}';
    }
}
