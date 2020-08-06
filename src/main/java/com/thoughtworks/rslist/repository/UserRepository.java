package com.thoughtworks.rslist.repository;


import com.thoughtworks.rslist.dto.UserDto;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserDto, Integer> {

    @Override
    Optional<UserDto> findById(Integer integer);

}
