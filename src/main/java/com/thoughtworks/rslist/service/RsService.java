package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.bean.RsEvent;
import com.thoughtworks.rslist.bean.User;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class RsService {

    private final UserService userService;
    private final List<RsEvent> rsList = new LinkedList<>();

    public RsService(UserService userService) {
        this.userService = userService;
        User root = new User("root", 20, "male", "root@thoughtworks.com", "12345678901");
        User testUser1 = new User("user1", 30, "female", "user1@thoughtworks.com", "12345678902");
        User testUser2 = new User("user2", 40, "male", "user2@thoughtworks.com", "12345678903");
        rsList.add(new RsEvent("美股熔断", "经济", testUser1));
        rsList.add(new RsEvent("边境冲突", "军事", root));
        rsList.add(new RsEvent("示威活动", "自由", testUser2));
    }

    public RsEvent getEvent(int id) {
        if (id < 1 || id > rsList.size()) {
            return null;
        }
        return rsList.get(id - 1);
    }

    public List<RsEvent> getEventList(Integer start, Integer end) {
        if (start == null || end == null) {
            return rsList;
        }
        if (start < 1 || end > rsList.size()) {
            return null;
        }
        return rsList.subList(start - 1, end);
    }

    public int addEvent(RsEvent rsEvent) {
        if (userService.getUserByUsername(rsEvent.getUser().getUserName()) == null) {
            userService.addNewUser(rsEvent.getUser());
        }
        rsList.add(rsEvent);
        return rsList.size();
    }

    public void updateEvent(int id, String name, String keyword) {
        if (name != null) {
            rsList.get(id - 1).setName(name);
        }
        if (keyword != null) {
            rsList.get(id - 1).setKeyword(keyword);
        }
    }

    public void deleteEventById(int id) {
        rsList.remove(id - 1);
    }

}
