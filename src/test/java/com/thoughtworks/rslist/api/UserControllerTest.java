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
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
                .andExpect(jsonPath("$[0].user_name", is("root")))
                .andExpect(jsonPath("$[0].user_age", is(20)))
                .andExpect(jsonPath("$[0].user_gender", is("male")))
                .andExpect(jsonPath("$[0].user_email", is("root@thoughtworks.com")))
                .andExpect(jsonPath("$[0].user_phone", is("12345678901")))
                .andExpect(jsonPath("$[1].user_name", is("user1")))
                .andExpect(jsonPath("$[1].user_age", is(30)))
                .andExpect(jsonPath("$[1].user_gender", is("female")))
                .andExpect(jsonPath("$[1].user_email", is("user1@thoughtworks.com")))
                .andExpect(jsonPath("$[1].user_phone", is("12345678902")))
                .andExpect(jsonPath("$[2].user_name", is("user2")))
                .andExpect(jsonPath("$[2].user_age", is(40)))
                .andExpect(jsonPath("$[2].user_gender", is("male")))
                .andExpect(jsonPath("$[2].user_email", is("user2@thoughtworks.com")))
                .andExpect(jsonPath("$[2].user_phone", is("12345678903")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    void should_return_user_by_username() throws Exception {
        mockMvc.perform(get("/rs/getUser?username=root"))
                .andExpect(jsonPath("$.user_name", is("root")))
                .andExpect(jsonPath("$.user_age", is(20)))
                .andExpect(jsonPath("$.user_gender", is("male")))
                .andExpect(jsonPath("$.user_email", is("root@thoughtworks.com")))
                .andExpect(jsonPath("$.user_phone", is("12345678901")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/getUser?username=user1"))
                .andExpect(jsonPath("$.user_name", is("user1")))
                .andExpect(jsonPath("$.user_age", is(30)))
                .andExpect(jsonPath("$.user_gender", is("female")))
                .andExpect(jsonPath("$.user_email", is("user1@thoughtworks.com")))
                .andExpect(jsonPath("$.user_phone", is("12345678902")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/getUser?username=user2"))
                .andExpect(jsonPath("$.user_name", is("user2")))
                .andExpect(jsonPath("$.user_age", is(40)))
                .andExpect(jsonPath("$.user_gender", is("male")))
                .andExpect(jsonPath("$.user_email", is("user2@thoughtworks.com")))
                .andExpect(jsonPath("$.user_phone", is("12345678903")))
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
                .andExpect(jsonPath("$.user_name", is("user3")))
                .andExpect(jsonPath("$.user_age", is(25)))
                .andExpect(jsonPath("$.user_gender", is("male")))
                .andExpect(jsonPath("$.user_email", is("user3@thoughtworks.com")))
                .andExpect(jsonPath("$.user_phone", is("12345678904")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    void should_get_bad_request_when_the_length_of_username_is_greater_than_eight() throws Exception {
        User user = new User("user4user4", 35, "female", "user4@thoughtworks.com", "12345678905");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStringOfUser = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/rs/addNewUser").contentType(MediaType.APPLICATION_JSON).content(jsonStringOfUser))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(5)
    void should_get_bad_request_when_username_is_null() throws Exception {
        User user = new User(null, 35, "female", "user4@thoughtworks.com", "12345678905");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStringOfUser = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/rs/addNewUser").contentType(MediaType.APPLICATION_JSON).content(jsonStringOfUser))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(6)
    void should_get_bad_request_when_gender_is_null() throws Exception {
        User user = new User("user4", 35, null, "user4@thoughtworks.com", "12345678905");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStringOfUser = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/rs/addNewUser").contentType(MediaType.APPLICATION_JSON).content(jsonStringOfUser))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(7)
    void should_get_bad_request_when_age_is_less_than_18() throws Exception {
        User user = new User("user4", 17, "female", "user4@thoughtworks.com", "12345678905");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStringOfUser = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/rs/addNewUser").contentType(MediaType.APPLICATION_JSON).content(jsonStringOfUser))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(8)
    void should_get_bad_request_when_age_is_greater_than_100() throws Exception {
        User user = new User("user4", 101, "female", "user4@thoughtworks.com", "12345678905");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStringOfUser = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/rs/addNewUser").contentType(MediaType.APPLICATION_JSON).content(jsonStringOfUser))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(9)
    void should_get_bad_request_when_email_is_invalid() throws Exception {
        User user = new User("user4", 35, "female", "user4thoughtworks.com", "12345678905");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStringOfUser = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/rs/addNewUser").contentType(MediaType.APPLICATION_JSON).content(jsonStringOfUser))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(10)
    void should_get_bad_request_when_phone_number_is_null() throws Exception {
        User user = new User("user4", 35, "female", "user4@thoughtworks.com", null);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStringOfUser = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/rs/addNewUser").contentType(MediaType.APPLICATION_JSON).content(jsonStringOfUser))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(11)
    void should_get_bad_request_when_phone_number_is_invalid() throws Exception {
        User user = new User("user4", 35, "female", "user4@thoughtworks.com", "2345678905");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStringOfUser = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/rs/addNewUser").contentType(MediaType.APPLICATION_JSON).content(jsonStringOfUser))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(12)
    void should_return_reformatted_json_string_of_user_list() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andReturn();
        String expectedJsonStringOfUserList = "[{\"user_name\":\"root\"," +
                "\"user_age\":20," +
                "\"user_gender\":\"male\"," +
                "\"user_email\":\"root@thoughtworks.com\"," +
                "\"user_phone\":\"12345678901\"}," +
                "{\"user_name\":\"user1\"," +
                "\"user_age\":30," +
                "\"user_gender\":\"female\"," +
                "\"user_email\":\"user1@thoughtworks.com\"," +
                "\"user_phone\":\"12345678902\"}," +
                "{\"user_name\":\"user2\"," +
                "\"user_age\":40," +
                "\"user_gender\":\"male\"," +
                "\"user_email\":\"user2@thoughtworks.com\"," +
                "\"user_phone\":\"12345678903\"}," +
                "{\"user_name\":\"user3\"," +
                "\"user_age\":25," +
                "\"user_gender\":\"male\"," +
                "\"user_email\":\"user3@thoughtworks.com\"," +
                "\"user_phone\":\"12345678904\"}]";
        assertEquals(expectedJsonStringOfUserList, mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
    }

    @Test
    @Order(13)
    void should_throw_an_exception_when_parameters_not_meet_the_requirement() throws Exception {
        User user1 = new User("user55555", 35, "female", "user5@thoughtworks.com", "12345678906");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStringOfUser1 = objectMapper.writeValueAsString(user1);
        mockMvc.perform(post("/rs/addNewUser").contentType(MediaType.APPLICATION_JSON).content(jsonStringOfUser1))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));

        User user2 = new User("user5", 16, "female", "user5@thoughtworks.com", "12345678906");
        String jsonStringOfUser2 = objectMapper.writeValueAsString(user2);
        mockMvc.perform(post("/rs/addNewUser").contentType(MediaType.APPLICATION_JSON).content(jsonStringOfUser2))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));

        User user3 = new User("user5", 35, null, "user5@thoughtworks.com", "12345678906");
        String jsonStringOfUser3 = objectMapper.writeValueAsString(user3);
        mockMvc.perform(post("/rs/addNewUser").contentType(MediaType.APPLICATION_JSON).content(jsonStringOfUser3))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));

        User user4 = new User("user5", 35, "female", "user5thoughtworks.com", "12345678906");
        String jsonStringOfUser4 = objectMapper.writeValueAsString(user4);
        mockMvc.perform(post("/rs/addNewUser").contentType(MediaType.APPLICATION_JSON).content(jsonStringOfUser4))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));

        User user5 = new User("user5", 35, "female", "user5@thoughtworks.com", "312345678906");
        String jsonStringOfUser5 = objectMapper.writeValueAsString(user5);
        mockMvc.perform(post("/rs/addNewUser").contentType(MediaType.APPLICATION_JSON).content(jsonStringOfUser5))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }


}
