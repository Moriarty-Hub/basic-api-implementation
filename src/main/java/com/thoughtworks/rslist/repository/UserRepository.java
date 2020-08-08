package com.thoughtworks.rslist.repository;


import com.thoughtworks.rslist.dto.UserDto;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<UserDto, Integer> {

    @Override
    Optional<UserDto> findById(Integer integer);

    @Override
    void deleteById(Integer integer);

    @Override
    List<UserDto> findAll();

    @Transactional
    @Modifying
    @Query("UPDATE UserDto userDto SET userDto.numberOfVotes = :number_of_votes WHERE userDto.id = :id")
    void updateVoteNumById(@Param("id") int id, @Param("number_of_votes") int numberOfVotes);

}
