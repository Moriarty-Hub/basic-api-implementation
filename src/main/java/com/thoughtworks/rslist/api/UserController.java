package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.bean.User;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

@RestController
public class UserController {

    private final List<User> userList = new LinkedList<>();

    public UserController() {
        User root = new User("root", 20, "male", "root@thoughtworks.com", "12345678901");
        User user1 = new User("user1", 30, "female", "user1@thoughtworks.com", "12345678902");
        User user2 = new User("user2", 40, "male", "user2@thoughtworks.com", "12345678903");
        userList.add(root);
        userList.add(user1);
        userList.add(user2);
    }


}
