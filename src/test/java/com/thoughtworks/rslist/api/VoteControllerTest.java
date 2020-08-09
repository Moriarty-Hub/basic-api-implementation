package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class VoteControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RsEventRepository rsEventRepository;
    @Autowired
    private VoteRepository voteRepository;

    @BeforeEach
    public void setUp() {
        UserDto userTestData1 = new UserDto("user1", 20, "male", "user1@gmail.com", "12345678901");
        UserDto userTestData2 = new UserDto("user2", 30, "female", "user2@gmail.com", "12345678902");
        userRepository.save(userTestData1);
        userRepository.save(userTestData2);

        RsEventDto eventTestData1 = new RsEventDto("name1", "keyword1", userTestData1);
        RsEventDto eventTestData2 = new RsEventDto("name2", "keyword2", userTestData2);
        rsEventRepository.save(eventTestData1);
        rsEventRepository.save(eventTestData2);

        VoteDto voteTestData1 = new VoteDto(LocalDateTime.now(), 5, eventTestData1, userTestData1);
        VoteDto voteTestData2 = new VoteDto(LocalDateTime.now(), 6, eventTestData2, userTestData2);
        voteRepository.save(voteTestData1);
        voteRepository.save(voteTestData2);
    }

    @AfterEach
    public void tearDown() {
        voteRepository.deleteAll();
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
    }

    @Test
    void should_return_vote_record() throws Exception {
        mockMvc.perform(get("/voteRecord"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].num", is(5)))
                .andExpect(jsonPath("$[0].userId", is(1)))
                .andExpect(jsonPath("$[0].rsEventId", is(1)))
                .andExpect(jsonPath("$[1].num", is(6)))
                .andExpect(jsonPath("$[1].userId", is(2)))
                .andExpect(jsonPath("$[1].rsEventId", is(2)))
                .andExpect(status().isOk());
    }

    @Test
    void should_return_vote_record_of_user2() throws Exception {
        mockMvc.perform(get("/voteRecord").param("userId", "2"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].num", is(6)))
                .andExpect(jsonPath("$[0].userId", is(2)))
                .andExpect(jsonPath("$[0].rsEventId", is(2)))
                .andExpect(status().isOk());
    }

    @Test
    void should_return_vote_record_of_event1() throws Exception {
        mockMvc.perform(get("/voteRecord").param("rsEventId", "1"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].num", is(5)))
                .andExpect(jsonPath("$[0].userId", is(1)))
                .andExpect(jsonPath("$[0].rsEventId", is(1)))
                .andExpect(status().isOk());
    }

    @Test
    void should_vote_to_specified_event() throws Exception {
        System.out.println(rsEventRepository.findAll().toString());
        System.out.println(userRepository.findAll().toString());
        Optional<UserDto> optionalUserDto = userRepository.findById(1);
        assertTrue(optionalUserDto.isPresent());
        UserDto userDto = optionalUserDto.get();
        assertEquals(10, userDto.getNumberOfVotes());

        Optional<RsEventDto> optionalRsEventDto = rsEventRepository.findById(1);
        assertTrue(optionalRsEventDto.isPresent());
        RsEventDto rsEventDto = optionalRsEventDto.get();
        assertEquals(0, rsEventDto.getVoteNum());

        assertEquals(2, voteRepository.count());

        mockMvc.perform(post("/rs/vote/1")
                .param("voteNum", "5")
                .param("userId", "1")
                .param("voteTime", LocalDateTime.now().toString()))
                .andExpect(status().isOk());

        Optional<UserDto> optionalUserDtoAfterVote = userRepository.findById(1);
        assertTrue(optionalUserDtoAfterVote.isPresent());
        UserDto userDtoAfterVote = optionalUserDtoAfterVote.get();
        assertEquals(5, userDtoAfterVote.getNumberOfVotes());

        Optional<RsEventDto> optionalRsEventDtoAfterVote = rsEventRepository.findById(1);
        assertTrue(optionalRsEventDtoAfterVote.isPresent());
        RsEventDto rsEventDtoAfterVote = optionalRsEventDtoAfterVote.get();
        assertEquals(5, rsEventDtoAfterVote.getVoteNum());

        assertEquals(3, voteRepository.count());
    }

    @Test
    void should_get_bad_request_when_user_votes_less_than_vote_num() throws Exception {
        Optional<UserDto> optionalUserDto = userRepository.findById(1);
        assertTrue(optionalUserDto.isPresent());
        UserDto userDto = optionalUserDto.get();
        assertEquals(10, userDto.getNumberOfVotes());

        Optional<RsEventDto> optionalRsEventDto = rsEventRepository.findById(1);
        assertTrue(optionalRsEventDto.isPresent());
        RsEventDto rsEventDto = optionalRsEventDto.get();
        assertEquals(0, rsEventDto.getVoteNum());

        assertEquals(2, voteRepository.count());

        mockMvc.perform(post("/rs/vote/1")
                .param("voteNum", "11")
                .param("userId", "1")
                .param("voteTime", LocalDateTime.now().toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("vote num is insufficient")));

        assertEquals(2, voteRepository.count());
    }

    @Test
    void should_return_vote_record_between_given_time() throws Exception {

        UserDto userTestData3 = new UserDto("user3", 40, "male", "user3@gmail.com", "12345678903");
        UserDto userTestData4 = new UserDto("user4", 50, "female", "user4@gmail.com", "12345678904");
        UserDto userTestData5 = new UserDto("user5", 60, "male", "user5@gmail.com", "12345678905");
        UserDto userTestData6 = new UserDto("user6", 70, "female", "user6@gmail.com", "12345678906");
        userRepository.save(userTestData3);
        userRepository.save(userTestData4);
        userRepository.save(userTestData5);
        userRepository.save(userTestData6);

        RsEventDto eventTestData3 = new RsEventDto("name3", "keyword3", userTestData3);
        RsEventDto eventTestData4 = new RsEventDto("name4", "keyword4", userTestData4);
        RsEventDto eventTestData5 = new RsEventDto("name5", "keyword5", userTestData5);
        RsEventDto eventTestData6 = new RsEventDto("name6", "keyword6", userTestData6);
        rsEventRepository.save(eventTestData3);
        rsEventRepository.save(eventTestData4);
        rsEventRepository.save(eventTestData5);
        rsEventRepository.save(eventTestData6);

        VoteDto voteTestData3 = new VoteDto(LocalDateTime.of(2020, 2, 8, 13, 0, 0),
                3, eventTestData3, userTestData3);
        VoteDto voteTestData4 = new VoteDto(LocalDateTime.of(2020, 2, 21, 8, 2, 26),
                8, eventTestData4, userTestData5);
        VoteDto voteTestData5 = new VoteDto(LocalDateTime.of(2020, 3, 2, 11, 10, 10),
                1, eventTestData5, userTestData6);
        VoteDto voteTestData6 = new VoteDto(LocalDateTime.of(2020, 3, 18, 2, 2, 0),
                9, eventTestData6, userTestData3);
        VoteDto voteTestData7 = new VoteDto(LocalDateTime.of(2020, 4, 11, 21, 45, 0),
                4, eventTestData3, userTestData4);
        VoteDto voteTestData8 = new VoteDto(LocalDateTime.of(2020, 4, 12, 20, 0, 0),
                6, eventTestData4, userTestData5);
        VoteDto voteTestData9 = new VoteDto(LocalDateTime.of(2020, 5, 10, 13, 1, 0),
                5, eventTestData5, userTestData6);
        VoteDto voteTestData10 = new VoteDto(LocalDateTime.of(2020, 5, 16, 12, 0, 0),
                5, eventTestData6, userTestData4);

        voteRepository.save(voteTestData3);
        voteRepository.save(voteTestData4);
        voteRepository.save(voteTestData5);
        voteRepository.save(voteTestData6);
        voteRepository.save(voteTestData7);
        voteRepository.save(voteTestData8);
        voteRepository.save(voteTestData9);
        voteRepository.save(voteTestData10);


        mockMvc.perform(get("/voteRecordByTime")
                .param("startYear", "2020").param("startMonth", "2")
                .param("startDay", "1").param("endYear", "2020")
                .param("endMonth", "5").param("endDay", "1"))
                .andExpect(jsonPath("$", hasSize(6)))
                .andExpect(jsonPath("$[0].userId", is(3)))
                .andExpect(jsonPath("$[0].rsEventId", is(3)))
                .andExpect(jsonPath("$[0].num", is(3)))
                .andExpect(jsonPath("$[1].userId", is(5)))
                .andExpect(jsonPath("$[1].rsEventId", is(4)))
                .andExpect(jsonPath("$[1].num", is(8)))
                .andExpect(jsonPath("$[2].userId", is(6)))
                .andExpect(jsonPath("$[2].rsEventId", is(5)))
                .andExpect(jsonPath("$[2].num", is(1)))
                .andExpect(jsonPath("$[3].userId", is(3)))
                .andExpect(jsonPath("$[3].rsEventId", is(6)))
                .andExpect(jsonPath("$[3].num", is(9)))
                .andExpect(jsonPath("$[4].userId", is(4)))
                .andExpect(jsonPath("$[4].rsEventId", is(3)))
                .andExpect(jsonPath("$[4].num", is(4)))
                .andExpect(jsonPath("$[5].userId", is(5)))
                .andExpect(jsonPath("$[5].rsEventId", is(4)))
                .andExpect(jsonPath("$[5].num", is(6)))
                .andExpect(status().isOk());
    }

}
