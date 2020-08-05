package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
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
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
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

    @Test
    @Order(2)
    void should_return_user_by_username() throws Exception {
        mockMvc.perform(get("/rs/getUser?username=root"))
                .andExpect(jsonPath("$.userName", is("root")))
                .andExpect(jsonPath("$.age", is(20)))
                .andExpect(jsonPath("$.gender", is("male")))
                .andExpect(jsonPath("$.email", is("root@thoughtworks.com")))
                .andExpect(jsonPath("$.phone", is("12345678901")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/getUser?username=user1"))
                .andExpect(jsonPath("$.userName", is("user1")))
                .andExpect(jsonPath("$.age", is(30)))
                .andExpect(jsonPath("$.gender", is("female")))
                .andExpect(jsonPath("$.email", is("user1@thoughtworks.com")))
                .andExpect(jsonPath("$.phone", is("12345678902")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/getUser?username=user2"))
                .andExpect(jsonPath("$.userName", is("user2")))
                .andExpect(jsonPath("$.age", is(40)))
                .andExpect(jsonPath("$.gender", is("male")))
                .andExpect(jsonPath("$.email", is("user2@thoughtworks.com")))
                .andExpect(jsonPath("$.phone", is("12345678903")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/getUser?username=user0"))
                .andExpect(jsonPath("$").doesNotExist())
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    void should_add_the_new_user_into_list() throws Exception {
        mockMvc.perform(get("/rs/getUser?username=user3"))
                .andExpect(jsonPath("$").doesNotExist())
                .andExpect(status().isOk());

        User user = new User("user3", 25, "male", "user3@thoughtworks.com", "12345678904");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStringOfUser = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/rs/addNewUser").contentType(MediaType.APPLICATION_JSON).content(jsonStringOfUser))
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
