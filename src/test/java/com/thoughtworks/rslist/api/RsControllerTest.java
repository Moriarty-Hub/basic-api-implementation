package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.bean.RsEvent;
import com.thoughtworks.rslist.bean.User;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.service.RsService;
import com.thoughtworks.rslist.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RsService rsService;
    @Autowired
    private UserService userService;
    @Autowired
    private RsEventRepository rsEventRepository;
    @Autowired
    private UserRepository userRepository;

    private static final int INITIAL_GENERATED_VALUE = 0;

    @BeforeEach
    public void setUp() {
        userService.addNewUser(new User("user1", 20, "male", "user1@gmail.com", "12345678901"));
        userService.addNewUser(new User("user2", 30, "female", "user2@gmail.com", "12345678902"));
        userService.addNewUser(new User("user3", 40, "male", "user3@gmail.com", "12345678903"));
        rsService.addEvent(new RsEvent("美股熔断", "经济", 1));
        rsService.addEvent(new RsEvent("边境冲突", "军事", 2));
        rsService.addEvent(new RsEvent("示威活动", "自由", 3));

        System.out.println(rsEventRepository.findAll().toString());
        System.out.println(userRepository.findAll().toString());
    }

    @AfterEach
    public void tearDown() {
        rsEventRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void should_return_rs_event_of_specified_index() throws Exception {
        System.out.println(rsEventRepository.findAll().toString());
        System.out.println(userRepository.findAll().toString());
        int id1 = INITIAL_GENERATED_VALUE + 1;
        mockMvc.perform(get("/rs/event/" + id1))
                .andExpect(jsonPath("$.name", is("美股熔断")))
                .andExpect(jsonPath("$.keyword", is("经济")))
                .andExpect(jsonPath("$.voteNum", is(0)))
                .andExpect(status().isOk())
                .andReturn();

        int id2 = INITIAL_GENERATED_VALUE + 2;
        mockMvc.perform(get("/rs/event/" + id2))
                .andExpect(jsonPath("$.name", is("边境冲突")))
                .andExpect(jsonPath("$.keyword", is("军事")))
                .andExpect(jsonPath("$.voteNum", is(0)))
                .andExpect(status().isOk())
                .andReturn();

        int id3 = INITIAL_GENERATED_VALUE + 3;
        mockMvc.perform(get("/rs/event/" + id3))
                .andExpect(jsonPath("$.name", is("示威活动")))
                .andExpect(jsonPath("$.keyword", is("自由")))
                .andExpect(jsonPath("$.voteNum", is(0)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void should_return_rs_event_list_between_specified_range() throws Exception {
        mockMvc.perform(get("/rs/eventList/start/1/end/3"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is("美股熔断")))
                .andExpect(jsonPath("$[0].keyword", is("经济")))
                .andExpect(jsonPath("$[0].voteNum", is(0)))
                .andExpect(jsonPath("$[1].name", is("边境冲突")))
                .andExpect(jsonPath("$[1].keyword", is("军事")))
                .andExpect(jsonPath("$[1].voteNum", is(0)))
                .andExpect(jsonPath("$[2].name", is("示威活动")))
                .andExpect(jsonPath("$[2].keyword", is("自由")))
                .andExpect(jsonPath("$[2].voteNum", is(0)))
                .andExpect(status().isOk())
                .andReturn();
        mockMvc.perform(get("/rs/eventList/start/1/end/2"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("美股熔断")))
                .andExpect(jsonPath("$[0].keyword", is("经济")))
                .andExpect(jsonPath("$[0].voteNum", is(0)))
                .andExpect(jsonPath("$[1].name", is("边境冲突")))
                .andExpect(jsonPath("$[1].keyword", is("军事")))
                .andExpect(jsonPath("$[1].voteNum", is(0)))
                .andExpect(status().isOk())
                .andReturn();
        mockMvc.perform(get("/rs/eventList/start/2/end/3"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("边境冲突")))
                .andExpect(jsonPath("$[0].keyword", is("军事")))
                .andExpect(jsonPath("$[0].voteNum", is(0)))
                .andExpect(jsonPath("$[1].name", is("示威活动")))
                .andExpect(jsonPath("$[1].keyword", is("自由")))
                .andExpect(jsonPath("$[1].voteNum", is(0)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void should_return_entire_rs_event_list() throws Exception {
        mockMvc.perform(get("/rs/eventList"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is("美股熔断")))
                .andExpect(jsonPath("$[0].keyword", is("经济")))
                .andExpect(jsonPath("$[0].voteNum", is(0)))
                .andExpect(jsonPath("$[1].name", is("边境冲突")))
                .andExpect(jsonPath("$[1].keyword", is("军事")))
                .andExpect(jsonPath("$[1].voteNum", is(0)))
                .andExpect(jsonPath("$[2].name", is("示威活动")))
                .andExpect(jsonPath("$[2].keyword", is("自由")))
                .andExpect(jsonPath("$[2].voteNum", is(0)))
                .andExpect(status().isOk());
    }

    @Test
    void should_add_the_given_event_into_event_list() throws Exception {
        mockMvc.perform(get("/rs/eventList")).andExpect(jsonPath("$", hasSize(3)));
        int id4 = INITIAL_GENERATED_VALUE + 4;
        mockMvc.perform(post("/rs/event")
                .param("name", "收割股民")
                .param("keyword", "民生")
                .param("userId", "1"))
                .andExpect(status().isCreated())
                .andExpect(header().string("index", String.valueOf(id4)));
        mockMvc.perform(get("/rs/eventList")).andExpect(jsonPath("$", hasSize(4)));
        mockMvc.perform(get("/rs/event/" + id4))
                .andExpect(jsonPath("$.name", is("收割股民")))
                .andExpect(jsonPath("$.keyword", is("民生")))
                .andExpect(jsonPath("$.voteNum", is(0)))
                .andExpect(status().isOk());
    }

    @Test
    void should_update_event_when_name_was_given() throws Exception {
        int id2 = INITIAL_GENERATED_VALUE + 2;
        mockMvc.perform(get("/rs/event/" + id2))
                .andExpect(jsonPath("$.name", is("边境冲突")))
                .andExpect(status().isOk());
        mockMvc.perform(patch("/rs/event/" + id2)
                .param("name", "收复台湾")
                .param("userId", "2"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/event/" + id2))
                .andExpect(jsonPath("$.name", is("收复台湾")));
    }

    @Test
    void should_update_event_when_keyword_was_given() throws Exception {
        int id3 = INITIAL_GENERATED_VALUE + 3;
        mockMvc.perform(get("/rs/event/" + id3))
                .andExpect(jsonPath("$.keyword", is("自由")));
        mockMvc.perform(patch("/rs/event/" + id3)
                .param("name", "")
                .param("keyword", "暴力")
                .param("userId", "3"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/event/" + id3))
                .andExpect(jsonPath("$.keyword", is("暴力")));
    }

    @Test
    void should_update_event_when_name_and_keyword_were_given() throws Exception {
        int id1 = INITIAL_GENERATED_VALUE + 1;
        mockMvc.perform(get("/rs/event/" + id1))
                .andExpect(jsonPath("$.name", is("美股熔断")))
                .andExpect(jsonPath("$.keyword", is("经济")));
        mockMvc.perform(patch("/rs/event/" + id1)
                .param("name", "奥运会推迟")
                .param("keyword", "体育")
                .param("userId", "1"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/event/" + id1))
                .andExpect(jsonPath("$.name", is("奥运会推迟")))
                .andExpect(jsonPath("$.keyword", is("体育")));
    }

    @Test
    void should_return_bad_request_when_the_given_user_id_is_unmatched_with_user_id_in_rs_event() throws Exception {
        int id3 = INITIAL_GENERATED_VALUE + 3;
        mockMvc.perform(get("/rs/event/" + id3))
                .andExpect(jsonPath("$.name", is("示威活动")))
                .andExpect(jsonPath("$.keyword", is("自由")))
                .andExpect(jsonPath("$.userId", is(3)));
        mockMvc.perform(patch("/rs/event/" + id3)
                .param("name", "示威游行")
                .param("keyword", "暴力")
                .param("userId", "8"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("unmatched user id")));
    }

    @Test
    void should_delete_the_specified_event_by_id() throws Exception {
        int id2 = INITIAL_GENERATED_VALUE + 2;
        mockMvc.perform(delete("/rs/event/" + id2))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/eventList"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("美股熔断")))
                .andExpect(jsonPath("$[0].keyword", is("经济")))
                .andExpect(jsonPath("$[1].name", is("示威活动")))
                .andExpect(jsonPath("$[1].keyword", is("自由")))
                .andExpect(status().isOk());
    }

    @Test
    void should_get_bad_request_when_event_name_is_null() throws Exception {
        String jsonStringOfRsEvent = "{\"keyword\":\"经济\", " +
                "\"user\": " +
                "\"userId\":\"1\"}";
        mockMvc.perform(post("/rs/event").contentType(MediaType.APPLICATION_JSON).content(jsonStringOfRsEvent))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_get_bad_request_when_keyword_is_null() throws Exception {
        String jsonStringOfRsEvent = "{\"name\":\"复工复产\", " +
                "\"user\": " +
                "{\"userName\":\"user3\", " +
                "\"age\": 25, " +
                "\"gender\": \"male\", " +
                "\"email\": \"user3@thoughtworks.com\", " +
                "\"phone\": \"12345678904\"}}";
        mockMvc.perform(post("/rs/event").contentType(MediaType.APPLICATION_JSON).content(jsonStringOfRsEvent))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_get_bad_request_when_user_is_null() throws Exception {
        String jsonStringOfRsEvent = "{\"name\":\"复工复产\", " +
                "\"keyword\":\"经济\"}}";
        mockMvc.perform(post("/rs/event").contentType(MediaType.APPLICATION_JSON).content(jsonStringOfRsEvent))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_throw_an_exception_when_start_or_end_out_of_boundary() throws Exception {
        mockMvc.perform(get("/rs/eventList/start/0/end/3"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid request param")));
        mockMvc.perform(get("/rs/eventList/start/2/end/8"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid request param")));
        mockMvc.perform(get("/rs/eventList/start/-1/end/10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid request param")));
    }

    @Test
    void should_throw_an_exception_when_index_out_of_boundary() throws Exception {
        int id0 = INITIAL_GENERATED_VALUE + 0;
        mockMvc.perform(get("/rs/event/" + id0))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid index")));
        int id20 = INITIAL_GENERATED_VALUE + 20;
        mockMvc.perform(get("/rs/event/" + id20))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid index")));
    }

    @Test
    void should_throw_an_exception_when_rs_event_parameters_not_meet_requirements() throws Exception {
        mockMvc.perform(post("/rs/event")
                .param("keyword", "经济")
                .param("userId", "3"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid param")));

        mockMvc.perform(post("/rs/event")
                .param("name", "复工复产")
                .param("userId", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid param")));

        mockMvc.perform(post("/rs/event")
                .param("name", "复工复产")
                .param("keyword", "经济"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid param")));

    }

    @Test
    void should_throw_an_exception_when_user_id_is_not_exist() throws Exception {
        mockMvc.perform(post("/rs/event")
                .param("name", "newRsEvent")
                .param("keyword", "newKeyword")
                .param("userId", "100"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("user not exist")));
    }

}