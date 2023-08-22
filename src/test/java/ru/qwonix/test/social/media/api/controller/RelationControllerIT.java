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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("/sql/relation_rest_controller/test_data.sql")
@Transactional
@SpringBootTest(classes = TestcontainersConfiguration.class)
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class RelationControllerIT {

    private static final String NON_EXISTENT_USERNAME = "werqw";
    private static final String USER_2_USERNAME = "user2";
    private static final String USER_3_USERNAME = "user3";

    @Autowired
    MockMvc mockMvc;

    @WithMockUser(username = "user1")
    @Test
    void addFriendHandler_NewFriendRequest_ResponseStatusIsCreated() throws Exception {
        var requestBuilder = post("/api/v1/user/" + USER_3_USERNAME + "/friend");

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isCreated()
        );
    }

    @WithMockUser(username = "user2")
    @Test
    void addFriendHandler_RepeatedRequest_ResponseStatusIsConflict() throws Exception {
        var requestBuilder = post("/api/v1/user/" + USER_3_USERNAME + "/friend");

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isConflict(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("""
                        {
                          "errorMessages": [
                            {
                              "field": "username",
                              "message": "request has already been sent"
                            }
                          ]
                        }
                        """)
        );
    }

    @WithMockUser(username = "user1")
    @Test
    void addFriendHandler_UsersAreAlreadyFriends_ResponseStatusIsConflict() throws Exception {
        var requestBuilder = post("/api/v1/user/" + USER_2_USERNAME + "/friend");

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isConflict(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("""
                        {
                          "errorMessages": [
                            {
                              "field": "username",
                              "message": "users are already friends"
                            }
                          ]
                        }
                        """)
        );
    }

    @WithMockUser(username = "user1")
    @Test
    void addFriendHandler_NonExistentUsername_ResponseStatusIsNotFound() throws Exception {
        var requestBuilder = post("/api/v1/user/" + NON_EXISTENT_USERNAME + "/friend");

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("""
                        {
                          "errorMessages": [
                            {
                              "field": "username",
                              "message": "user doesn't exist"
                            }
                          ]
                        }
                        """)
        );
    }

    @WithMockUser(username = "user1")
    @Test
    void removeFriendHandler_UsersAreFriends_ResponseStatusIsOk() throws Exception {
        var requestBuilder = delete("/api/v1/user/" + USER_2_USERNAME + "/friend");

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk()
        );
    }

    @WithMockUser(username = "user1")
    @Test
    void removeFriendHandler_UsersAreNotFriends_ResponseStatusIsNotFound() throws Exception {
        var requestBuilder = delete("/api/v1/user/" + USER_3_USERNAME + "/friend");

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("""
                        {
                          "errorMessages": [
                            {
                              "field": "username",
                              "message": "users are not friends"
                            }
                          ]
                        }
                        """)
        );
    }

    @WithMockUser(username = "user1")
    @Test
    void removeFriendHandler_NonExistentUsername_ResponseStatusIsNotFound() throws Exception {
        var requestBuilder = delete("/api/v1/user/" + NON_EXISTENT_USERNAME + "/friend");

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("""
                        {
                          "errorMessages": [
                            {
                              "field": "username",
                              "message": "user doesn't exist"
                            }
                          ]
                        }
                        """)
        );
    }
}