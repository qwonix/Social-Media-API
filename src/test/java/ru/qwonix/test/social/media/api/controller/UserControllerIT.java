package ru.qwonix.test.social.media.api.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.qwonix.test.social.media.api.TestcontainersConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("/sql/user_rest_controller/test_data.sql")
@SpringBootTest(classes = TestcontainersConfiguration.class)
@Transactional
@AutoConfigureMockMvc
class UserControllerIT {

    @Autowired
    MockMvc mockMvc;


    @WithMockUser(username = "user1")
    @Test
    void handleGetByUsername_GetItself_ReturnsFullUserProfile() throws Exception {
        var requestBuilder = get("/api/v1/user/profile/user1");

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("""
                        {
                          "id": "7cfb092f-e7df-497b-bcf3-f4c7c6ebb341",
                          "username": "user1",
                          "email": "user1@example.com"
                        }
                        """)
        );
    }

    @WithMockUser(username = "user1")
    @Test
    void handleGetByUsername_GetAnotherUser_ReturnsPublicUserProfile() throws Exception {
        var requestBuilder = get("/api/v1/user/profile/user2");

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("""
                        {
                          "username": "user2"
                        }
                        """)
        );
    }

    @Test
    void handleGetByUsername_NoAuthentication_ReturnsValidErrorResponse() throws Exception {
        var requestBuilder = get("/api/v1/user/profile/user1");

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isUnauthorized()
        );
    }
}