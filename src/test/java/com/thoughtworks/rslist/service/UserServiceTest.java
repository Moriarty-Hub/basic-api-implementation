package com.thoughtworks.rslist.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.bean.User;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setUp() {
        UserDto userTestData1 = new UserDto("user1", 20, "male", "user1@gmail.com", "12345678901");
        UserDto userTestData2 = new UserDto("user2", 30, "female", "user2@gmail.com", "12345678902");
        UserDto userTestData3 = new UserDto("user3", 40, "male", "user3@gmail.com", "12345678903");
        userRepository.save(userTestData1);
        userRepository.save(userTestData2);
        userRepository.save(userTestData3);
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void should_add_new_user() {
        User user = new User("user4", 50, "female", "user4@gmail.com", "12345678904");
        int id = userService.addNewUser(user);
        Optional<UserDto> optionalUserDto = userRepository.findById(id);
        assertTrue(optionalUserDto.isPresent());
        UserDto userDto = optionalUserDto.get();
        assertEquals(id, userDto.getId());
        assertEquals("user4", userDto.getUsername());
        assertEquals(50, userDto.getAge());
        assertEquals("female", userDto.getGender());
        assertEquals("user4@gmail.com", userDto.getEmail());
        assertEquals("12345678904", userDto.getPhone());
    }

    @Test
    void should_return_user_by_username_when_username_exist() {
        User user = userService.getUserByUsername("user2");
        assertNotNull(user);
        assertEquals("user2", user.getUserName());
        assertEquals(30, user.getAge());
        assertEquals("female", user.getGender());
        assertEquals("user2@gmail.com", user.getEmail());
        assertEquals("12345678902", user.getPhone());
    }

    @Test
    void should_return_null_by_username_when_username_not_exist() {
        assertNull(userService.getUserByUsername("NotExistUser"));
    }

    @Test
    void should_return_user_list() {
        List<User> userList = userService.getUserList();
        assertEquals(3, userList.size());
        User user = userList.get(1);
        assertNotNull(user);
        assertEquals("user2", user.getUserName());
        assertEquals(30, user.getAge());
        assertEquals("female", user.getGender());
        assertEquals("user2@gmail.com", user.getEmail());
        assertEquals("12345678902", user.getPhone());
    }

    @Test
    void should_get_user_list_of_json_string() throws JsonProcessingException {
        List<User> expectedUserList = new LinkedList<>();
        expectedUserList.add(new User("user1", 20, "male", "user1@gmail.com", "12345678901"));
        expectedUserList.add(new User("user2", 30, "female", "user2@gmail.com", "12345678902"));
        expectedUserList.add(new User("user3", 40, "male", "user3@gmail.com", "12345678903"));
        ObjectMapper objectMapper = new ObjectMapper();
        String expectedJsonString = objectMapper.writeValueAsString(expectedUserList);
        assertEquals(expectedJsonString, userService.getUserListOfJsonFormat());
    }

    @Test
    void should_get_user_when_user_id_exist() {
        User user = userService.getUserByUserId(2);
        assertNotNull(user);
        assertEquals("user2", user.getUserName());
        assertEquals(30, user.getAge());
        assertEquals("female", user.getGender());
        assertEquals("user2@gmail.com", user.getEmail());
        assertEquals("12345678902", user.getPhone());
    }

    @Test
    void should_get_null_when_user_id_not_exist() {
        assertNull(userService.getUserByUserId(10));
    }

}
