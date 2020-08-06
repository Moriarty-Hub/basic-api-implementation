package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.dto.UserDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        UserDto testUserDto1 = new UserDto("newUser1", 36, "male", "newUser1@thoughtworks.com", "12345678909");
        UserDto testUserDto2 = new UserDto("newUser2", 37, "female", "newUser2@thoughtworks.com", "12345678908");
        UserDto testUserDto3 = new UserDto("newUser3", 38, "male", "newUser3@thoughtworks.com", "12345678907");
        userRepository.save(testUserDto1);
        userRepository.save(testUserDto2);
        userRepository.save(testUserDto3);
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @Order(1)
    void should_save_user_into_database() {
        UserDto userDto = new UserDto("newUser", 36, "male", "newUser@thoughtworks.com", "12345678909");
        int id = 7;

        assertFalse(userRepository.findById(id).isPresent());
        userRepository.save(userDto);

        Optional<UserDto> optionalUserDto = userRepository.findById(id);
        assertTrue(optionalUserDto.isPresent());
        assertEquals(userDto.toString(), optionalUserDto.get().toString());
    }

    @Test
    @Order(2)
    void should_delete_user_by_id() {
        assertTrue(userRepository.findById(3).isPresent());
        userRepository.deleteById(3);
        assertFalse(userRepository.findById(3).isPresent());

        assertTrue(userRepository.findById(2).isPresent());
        userRepository.deleteById(2);
        assertFalse(userRepository.findById(2).isPresent());
    }
}
