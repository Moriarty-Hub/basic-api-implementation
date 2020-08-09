package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thoughtworks.rslist.bean.User;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/rs/userList")
    public ResponseEntity<List<User>> getEntireUserList() {
        return ResponseEntity.ok(userService.getUserList());
    }

    @GetMapping("/rs/user/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @PostMapping("/rs/user")
    public ResponseEntity<String> addNewUser(@RequestBody @Valid User user) {
        int index = userService.addNewUser(user);
        return ResponseEntity.created(null).header("index", String.valueOf(index)).build();
    }

    @GetMapping("/jsonFormattedUserList")
    public ResponseEntity<String> getUserListOfJsonFormat() throws JsonProcessingException {
        return ResponseEntity.ok(userService.getUserListOfJsonFormat());
    }

    @DeleteMapping("/rs/user/{id}")
    public void deleteUserById(@PathVariable int id) {
        userService.deleteUserById(id);
    }
}
