package com.thoughtworks.rslist;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RsListApplicationTests {

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
        String jsonStringOfNewEvent = "{\"name\": \"收割股民\", \"keyword\": \"民生\"}";
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

}
