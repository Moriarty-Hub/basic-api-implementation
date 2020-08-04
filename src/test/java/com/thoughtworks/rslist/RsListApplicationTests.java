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
                .andExpect(content().string("第一条事件"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/getEvent?id=2"))
                .andExpect(content().string("第二条事件"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/getEvent?id=3"))
                .andExpect(content().string("第三条事件"))
                .andExpect(status().isOk());
    }

    @Test
    void should_return_rs_event_list_between_specified_range() throws Exception {
        mockMvc.perform(get("/rs/getEventList?start=1&end=3"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0]", is("第一条事件")))
                .andExpect(jsonPath("$[1]", is("第二条事件")))
                .andExpect(jsonPath("$[2]", is("第三条事件")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/getEventList?start=1&end=2"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]", is("第一条事件")))
                .andExpect(jsonPath("$[1]", is("第二条事件")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/getEventList?start=2&end=3"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]", is("第二条事件")))
                .andExpect(jsonPath("$[1]", is("第三条事件")))
                .andExpect(status().isOk());
    }

}
