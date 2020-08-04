package com.thoughtworks.rslist;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RsListApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
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

}
