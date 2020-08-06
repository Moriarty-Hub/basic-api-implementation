package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.bean.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("/rs/getAllUsers")
    public ResponseEntity<List<User>> getEntireUserList() {
        return ResponseEntity.ok(userList);
    }

    @GetMapping("/rs/getUser")
    public ResponseEntity<User> getUserByUsername(@RequestParam String username) {
        Optional<User> optionalUser = userList.stream().filter(user -> user.getUserName().equals(username)).findFirst();
        return optionalUser.map(ResponseEntity::ok).orElse(null);
    }

    @PostMapping("/rs/addNewUser")
    public ResponseEntity<String> addNewUser(@RequestBody @Valid User user) {
        userList.add(user);
        return ResponseEntity.created(null).header("index", String.valueOf(userList.size())).build();
    }

    @GetMapping("/users")
    public ResponseEntity<String> getUserListOfJsonFormat() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return ResponseEntity.ok(objectMapper.writeValueAsString(userList));
    }
}
