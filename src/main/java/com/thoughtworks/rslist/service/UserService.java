package com.thoughtworks.rslist.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.bean.User;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final List<User> userList = new LinkedList<>();

    public UserService() {
        User root = new User("root", 20, "male", "root@thoughtworks.com", "12345678901");
        User testUser1 = new User("user1", 30, "female", "user1@thoughtworks.com", "12345678902");
        User testUser2 = new User("user2", 40, "male", "user2@thoughtworks.com", "12345678903");
        userList.add(root);
        userList.add(testUser1);
        userList.add(testUser2);
    }

    public int addNewUser(User user) {
        userList.add(user);
        return userList.size();
    }

    public User getUserByUsername(String username) {
        Optional<User> optionalUser = userList.stream().filter(user -> user.getUserName().equals(username)).findFirst();
        return optionalUser.orElse(null);
    }

    public List<User> getUserList() {
        return userList;
    }

    public String getUserListOfJsonFormat() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(userList);
    }
}
