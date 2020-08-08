package com.thoughtworks.rslist.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.bean.User;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public int addNewUser(User user) {
        UserDto userDto = userRepository.save(new UserDto(user.getUserName(), user.getAge(), user.getGender(), user.getEmail(), user.getPhone()));
        return userDto.getId();
    }

    public User getUserByUsername(String username) {
        List<UserDto> userDtoList = userRepository.findAll();
        for (UserDto userDto : userDtoList) {
            if (userDto.getUsername().equals(username)) {
                return new User(userDto.getUsername(), userDto.getAge(), userDto.getGender(), userDto.getEmail(), userDto.getPhone());
            }
        }
        return null;
    }

    public List<User> getUserList() {
        List<User> returnedList = new LinkedList<>();
        List<UserDto> userDtoList = userRepository.findAll();
        for (UserDto userDto : userDtoList) {
            returnedList.add(new User(userDto.getUsername(), userDto.getAge(), userDto.getGender(), userDto.getEmail(), userDto.getPhone()));
        }
        return returnedList;
    }

    public String getUserListOfJsonFormat() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(getUserList());
    }

    public User getUserByUserId(int id) {
        Optional<UserDto> optionalUserDto = userRepository.findById(id);
        if (optionalUserDto.isPresent()) {
            UserDto userDto = optionalUserDto.get();
            return new User(userDto.getUsername(), userDto.getAge(), userDto.getGender(), userDto.getEmail(), userDto.getPhone());
        }
        return null;
    }

    public void deleteUserById(int id) {
        userRepository.deleteById(id);
    }
}
