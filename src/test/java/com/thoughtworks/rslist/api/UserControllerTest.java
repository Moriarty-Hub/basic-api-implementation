package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.bean.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_return_entire_user_list() throws Exception {
        mockMvc.perform(get("/rs/getAllUsers"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].userName", is("root")))
                .andExpect(jsonPath("$[0].age", is(20)))
                .andExpect(jsonPath("$[0].gender", is("male")))
                .andExpect(jsonPath("$[0].email", is("root@thoughtworks.com")))
                .andExpect(jsonPath("$[0].phone", is("12345678901")))
                .andExpect(jsonPath("$[1].userName", is("user1")))
                .andExpect(jsonPath("$[1].age", is(30)))
                .andExpect(jsonPath("$[1].gender", is("female")))
                .andExpect(jsonPath("$[1].email", is("user1@thoughtworks.com")))
                .andExpect(jsonPath("$[1].phone", is("12345678902")))
                .andExpect(jsonPath("$[2].userName", is("user2")))
                .andExpect(jsonPath("$[2].age", is(40)))
                .andExpect(jsonPath("$[2].gender", is("male")))
                .andExpect(jsonPath("$[2].email", is("user2@thoughtworks.com")))
                .andExpect(jsonPath("$[2].phone", is("12345678903")))
                .andExpect(status().isOk());
    }
}
