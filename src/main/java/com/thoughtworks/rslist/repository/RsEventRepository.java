package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.dto.RsEventDto;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface RsEventRepository extends CrudRepository<RsEventDto, Integer> {

    @Override
    Optional<RsEventDto> findById(Integer integer);

    @Override
    List<RsEventDto> findAll();

    @Override
    void deleteById(Integer integer);

    @Transactional
    @Modifying
    @Query("UPDATE RsEventDto rsEventDto SET rsEventDto.name = :name, rsEventDto.keyword = :keyword WHERE rsEventDto.id = :id")
    void updateNameAndKeywordById(@Param("id") int id, @Param("name") String name, @Param("keyword") String keyword);

    @Override
    long count();

}
