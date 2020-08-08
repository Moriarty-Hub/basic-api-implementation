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

}
