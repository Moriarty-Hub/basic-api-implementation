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

    @GetMapping("/rs/getAllUsers")
    public ResponseEntity<List<User>> getEntireUserList() {
        return ResponseEntity.ok(userService.getUserList());
    }

    @GetMapping("/rs/getUser")
    public ResponseEntity<User> getUserByUsername(@RequestParam String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @PostMapping("/rs/addNewUser")
    public ResponseEntity<String> addNewUser(@RequestBody @Valid User user) {
        int index = userService.addNewUser(user);
        return ResponseEntity.created(null).header("index", String.valueOf(index)).build();
    }

    @GetMapping("/users")
    public ResponseEntity<String> getUserListOfJsonFormat() throws JsonProcessingException {
        return ResponseEntity.ok(userService.getUserListOfJsonFormat());
    }

    @GetMapping("/rs/deleteUser")
    public void deleteUserById(@RequestParam int id) {
        userService.deleteUserById(id);
    }
}
