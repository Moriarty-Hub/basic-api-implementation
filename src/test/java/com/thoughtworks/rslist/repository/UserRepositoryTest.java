package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.bean.User;
import com.thoughtworks.rslist.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void should_save_user_into_database() {
        UserDto userDto = new UserDto("newUser", 36, "male", "newUser@thoughtworks.com", "12345678909");
        int id = 1;

        assertFalse(userRepository.findById(id).isPresent());
        userRepository.save(userDto);

        Optional<UserDto> optionalUserDto = userRepository.findById(id);
        assertTrue(optionalUserDto.isPresent());
        assertEquals(userDto.toString(), optionalUserDto.get().toString());
    }
}
