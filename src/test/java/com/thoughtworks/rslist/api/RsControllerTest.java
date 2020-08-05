package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.bean.RsEvent;
import com.thoughtworks.rslist.bean.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    void should_return_rs_event_of_specified_index() throws Exception {
        mockMvc.perform(get("/rs/getEvent?id=1"))
                .andExpect(jsonPath("$.name", is("美股熔断")))
                .andExpect(jsonPath("$.keyword", is("经济")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/getEvent?id=2"))
                .andExpect(jsonPath("$.name", is("边境冲突")))
                .andExpect(jsonPath("$.keyword", is("军事")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/getEvent?id=3"))
                .andExpect(jsonPath("$.name", is("示威活动")))
                .andExpect(jsonPath("$.keyword", is("自由")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    void should_return_rs_event_list_between_specified_range() throws Exception {
        mockMvc.perform(get("/rs/getEventList?start=1&end=3"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is("美股熔断")))
                .andExpect(jsonPath("$[0].keyword", is("经济")))
                .andExpect(jsonPath("$[1].name", is("边境冲突")))
                .andExpect(jsonPath("$[1].keyword", is("军事")))
                .andExpect(jsonPath("$[2].name", is("示威活动")))
                .andExpect(jsonPath("$[2].keyword", is("自由")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/getEventList?start=1&end=2"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("美股熔断")))
                .andExpect(jsonPath("$[0].keyword", is("经济")))
                .andExpect(jsonPath("$[1].name", is("边境冲突")))
                .andExpect(jsonPath("$[1].keyword", is("军事")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/getEventList?start=2&end=3"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("边境冲突")))
                .andExpect(jsonPath("$[0].keyword", is("军事")))
                .andExpect(jsonPath("$[1].name", is("示威活动")))
                .andExpect(jsonPath("$[1].keyword", is("自由")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    void should_return_entire_rs_event_list() throws Exception {
        mockMvc.perform(get("/rs/getEventList"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is("美股熔断")))
                .andExpect(jsonPath("$[0].keyword", is("经济")))
                .andExpect(jsonPath("$[1].name", is("边境冲突")))
                .andExpect(jsonPath("$[1].keyword", is("军事")))
                .andExpect(jsonPath("$[2].name", is("示威活动")))
                .andExpect(jsonPath("$[2].keyword", is("自由")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    void should_add_the_given_event_into_event_list() throws Exception {
        mockMvc.perform(get("/rs/getEventList")).andExpect(jsonPath("$", hasSize(3)));
        String jsonStringOfNewEvent = "{\"name\":\"收割股民\", " +
                "\"keyword\":\"民生\", " +
                "\"user\": " +
                "{\"userName\":\"root\", " +
                "\"age\": 20, " +
                "\"gender\": \"male\", " +
                "\"email\": \"root@thoughtworks.com\", " +
                "\"phone\": \"12345678901\"}}";
        mockMvc.perform(post("/rs/addEvent").contentType(MediaType.APPLICATION_JSON).content(jsonStringOfNewEvent))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/getEventList")).andExpect(jsonPath("$", hasSize(4)));
        mockMvc.perform(get("/rs/getEvent?id=4"))
                .andExpect(jsonPath("$.name", is("收割股民")))
                .andExpect(jsonPath("$.keyword", is("民生")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    void should_update_event_when_name_was_given() throws Exception {
        mockMvc.perform(get("/rs/getEvent?id=2"))
                .andExpect(jsonPath("$.name", is("边境冲突")));
        mockMvc.perform(get("/rs/updateEvent?id=2&name=收复台湾"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/getEvent?id=2"))
                .andExpect(jsonPath("$.name", is("收复台湾")));
    }

    @Test
    @Order(6)
    void should_update_event_when_keyword_was_given() throws Exception {
        mockMvc.perform(get("/rs/getEvent?id=3"))
                .andExpect(jsonPath("$.keyword", is("自由")));
        mockMvc.perform(get("/rs/updateEvent?id=3&keyword=暴力"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/getEvent?id=3"))
                .andExpect(jsonPath("$.keyword", is("暴力")));
    }

    @Test
    @Order(7)
    void should_update_event_when_name_and_keyword_were_given() throws Exception {
        mockMvc.perform(get("/rs/getEvent?id=1"))
                .andExpect(jsonPath("$.name", is("美股熔断")))
                .andExpect(jsonPath("$.keyword", is("经济")));
        mockMvc.perform(get("/rs/updateEvent?id=1&name=奥运会推迟&keyword=体育"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/getEvent?id=1"))
                .andExpect(jsonPath("$.name", is("奥运会推迟")))
                .andExpect(jsonPath("$.keyword", is("体育")));
    }

    @Test
    @Order(8)
    void should_delete_the_specified_event_by_id() throws Exception {
        mockMvc.perform(get("/rs/deleteEvent?id=2"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/getEventList"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is("奥运会推迟")))
                .andExpect(jsonPath("$[0].keyword", is("体育")))
                .andExpect(jsonPath("$[1].name", is("示威活动")))
                .andExpect(jsonPath("$[1].keyword", is("暴力")))
                .andExpect(jsonPath("$[2].name", is("收割股民")))
                .andExpect(jsonPath("$[2].keyword", is("民生")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(9)
    void should_add_the_rs_event_with_attached_user_into_list() throws Exception {
        User user = new User("root", 20, "male", "root@thoughtworks.com", "12345678901");
        RsEvent rsEvent = new RsEvent("黎巴嫩爆炸", "安全", user);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStringOfRsEvent = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/addEvent").contentType(MediaType.APPLICATION_JSON).content(jsonStringOfRsEvent))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/getEventList"))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/getEvent?id=4"))
                .andExpect(jsonPath("$.name", is("黎巴嫩爆炸")))
                .andExpect(jsonPath("$.keyword", is("安全")))
                .andExpect(jsonPath("$.user.userName", is("root")))
                .andExpect(jsonPath("$.user.age", is(20)))
                .andExpect(jsonPath("$.user.gender", is("male")))
                .andExpect(jsonPath("$.user.email", is("root@thoughtworks.com")))
                .andExpect(jsonPath("$.user.phone", is("12345678901")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(10)
    void should_add_the_event_and_user_into_respective_list() throws Exception {
        mockMvc.perform(get("/rs/getUser?username=user3"))
                .andExpect(jsonPath("$").doesNotExist())
                .andExpect(status().isOk());
        String jsonStringOfRsEvent = "{\"name\":\"电影院复工\", " +
                "\"keyword\":\"娱乐\", " +
                "\"user\": " +
                "{\"userName\":\"user3\", " +
                "\"age\": 25, " +
                "\"gender\": \"male\", " +
                "\"email\": \"user3@thoughtworks.com\", " +
                "\"phone\": \"12345678904\"}}";
        mockMvc.perform(post("/rs/addEvent").contentType(MediaType.APPLICATION_JSON).content(jsonStringOfRsEvent))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/getEvent?id=5"))
                .andExpect(jsonPath("$.name", is("电影院复工")))
                .andExpect(jsonPath("$.keyword", is("娱乐")))
                .andExpect(jsonPath("$.user.userName", is("user3")))
                .andExpect(jsonPath("$.user.age", is(25)))
                .andExpect(jsonPath("$.user.gender", is("male")))
                .andExpect(jsonPath("$.user.email", is("user3@thoughtworks.com")))
                .andExpect(jsonPath("$.user.phone", is("12345678904")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/getUser?username=user3"))
                .andExpect(jsonPath("$.userName", is("user3")))
                .andExpect(jsonPath("$.age", is(25)))
                .andExpect(jsonPath("$.gender", is("male")))
                .andExpect(jsonPath("$.email", is("user3@thoughtworks.com")))
                .andExpect(jsonPath("$.phone", is("12345678904")))
                .andExpect(status().isOk());
    }

}