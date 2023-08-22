package ru.qwonix.test.social.media.api.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.qwonix.test.social.media.api.TestcontainersConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql("/sql/chat_rest_controller/test_data.sql")
@SpringBootTest(classes = TestcontainersConfiguration.class)
@Transactional
@AutoConfigureMockMvc
class ChatControllerIT {

    private static final String NON_EXISTENT_USERNAME = "non";
    private static final String USER_1_USERNAME = "user1";
    private static final String USER_2_USERNAME = "user2";
    private static final String USER_3_USERNAME = "user3";
    @Autowired
    MockMvc mockMvc;

    @WithMockUser(username = "user1")
    @Test
    void handleSendMessage_ValidMessage_ReturnValidResponse() throws Exception {
        var requestBuilder = post("/api/v1/chat/" + USER_2_USERNAME + "/message")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "text": "Another message"
                        }
                        """);

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.sender.username").value("user1"),
                jsonPath("$.recipient.username").value("user2"),
                jsonPath("$.text").value("Another message"),
                jsonPath("$.sendingTime").isString()
        );
    }

    @WithMockUser(username = "user1")
    @Test
    void handleSendMessage_EmptyText_ReturnErrorMessage() throws Exception {
        var requestBuilder = post("/api/v1/chat/" + USER_2_USERNAME + "/message")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "text": ""
                        }
                        """);

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest(),
                header().doesNotExist(HttpHeaders.LOCATION),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("""
                        {
                            "errorMessages": [{
                                    "field": "text",
                                    "message": "the text cannot be blank"
                                }
                            ]
                        }
                        """)
        );
    }

    @WithMockUser(username = "user1")
    @Test
    void handleSendMessage_RecipientNotFound_ReturnErrorMessage() throws Exception {
        var requestBuilder = post("/api/v1/chat/" + NON_EXISTENT_USERNAME + "/message")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "text": "Message"
                        }
                        """);

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("""
                        {
                          "errorMessages": [
                            {
                              "field": "recipient",
                              "message": "user doesn't exist"
                            }
                          ]
                        }
                        """)
        );
    }


    @WithMockUser(username = "user2")
    @Test
    void handleSendMessage_UsersAreNonFriends_ReturnErrorMessage() throws Exception {
        var requestBuilder = post("/api/v1/chat/" + USER_3_USERNAME + "/message")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "text": "Message"
                        }
                        """);

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("""
                        {
                          "errorMessages": [
                            {
                              "field": "relation",
                              "message": "you have to be a friend to send a message"
                            }
                          ]
                        }
                        """)
        );
    }

    @WithMockUser(username = "user1")
    @Test
    void handleGetChat_AllMessages_ReturnValidResponse() throws Exception {
        var requestBuilder = get("/api/v1/chat/" + USER_2_USERNAME);

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("""
                        [{
                                 "sender": {
                                     "username": "user1"
                                 },
                                 "recipient": {
                                     "username": "user2"
                                 },
                                 "text": "Fifth message from user1",
                                 "sendingTime": "2023-08-19T15:13:00"
                             }, {
                                 "sender": {
                                     "username": "user1"
                                 },
                                 "recipient": {
                                     "username": "user2"
                                 },
                                 "text": "Fourth message from user1",
                                 "sendingTime": "2023-08-19T15:12:00"
                             }, {
                                 "sender": {
                                     "username": "user1"
                                 },
                                 "recipient": {
                                     "username": "user2"
                                 },
                                 "text": "Third message from user1",
                                 "sendingTime": "2023-08-19T15:11:00"
                             }, {
                                 "sender": {
                                     "username": "user1"
                                 },
                                 "recipient": {
                                     "username": "user2"
                                 },
                                 "text": "Second message from user1",
                                 "sendingTime": "2023-08-19T15:10:00"
                             }, {
                                 "sender": {
                                     "username": "user2"
                                 },
                                 "recipient": {
                                     "username": "user1"
                                 },
                                 "text": "First message from user2",
                                 "sendingTime": "2023-08-19T15:05:00"
                             }, {
                                 "sender": {
                                     "username": "user1"
                                 },
                                 "recipient": {
                                     "username": "user2"
                                 },
                                 "text": "First message from user1",
                                 "sendingTime": "2023-08-19T15:00:00"
                             }
                        ]
                        """, true)

        );
    }

    @WithMockUser(username = "user2")
    @Test
    void handleGetChat_AllMessagesRequestByUser2_ReturnValidResponse() throws Exception {
        var requestBuilder = get("/api/v1/chat/" + USER_1_USERNAME);

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("""
                        [{
                                "sender": {
                                    "username": "user1"
                                },
                                "recipient": {
                                    "username": "user2"
                                },
                                "text": "Fifth message from user1",
                                "sendingTime": "2023-08-19T15:13:00"
                            }, {
                                "sender": {
                                    "username": "user1"
                                },
                                "recipient": {
                                    "username": "user2"
                                },
                                "text": "Fourth message from user1",
                                "sendingTime": "2023-08-19T15:12:00"
                            }, {
                                "sender": {
                                    "username": "user1"
                                },
                                "recipient": {
                                    "username": "user2"
                                },
                                "text": "Third message from user1",
                                "sendingTime": "2023-08-19T15:11:00"
                            }, {
                                "sender": {
                                    "username": "user1"
                                },
                                "recipient": {
                                    "username": "user2"
                                },
                                "text": "Second message from user1",
                                "sendingTime": "2023-08-19T15:10:00"
                            }, {
                                "sender": {
                                    "username": "user2"
                                },
                                "recipient": {
                                    "username": "user1"
                                },
                                "text": "First message from user2",
                                "sendingTime": "2023-08-19T15:05:00"
                            }, {
                                "sender": {
                                    "username": "user1"
                                },
                                "recipient": {
                                    "username": "user2"
                                },
                                "text": "First message from user1",
                                "sendingTime": "2023-08-19T15:00:00"
                            }
                        ]
                        """, true)

        );
    }

    @WithMockUser(username = "user1")
    @Test
    void handleGetChat_UsingPageAndSize_ReturnValidResponse() throws Exception {
        var requestBuilder = get("/api/v1/chat/" + USER_2_USERNAME)
                .queryParam("page", "0")
                .queryParam("count", "2");

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("""
                        [{
                                 "sender": {
                                     "username": "user1"
                                 },
                                 "recipient": {
                                     "username": "user2"
                                 },
                                 "text": "Fifth message from user1",
                                 "sendingTime": "2023-08-19T15:13:00"
                             }, {
                                 "sender": {
                                     "username": "user1"
                                 },
                                 "recipient": {
                                     "username": "user2"
                                 },
                                 "text": "Fourth message from user1",
                                 "sendingTime": "2023-08-19T15:12:00"
                             }
                        ]
                        """, true)

        );
    }

    @WithMockUser(username = "user1")
    @Test
    void handleGetChat_UsingPageAndSize2_ReturnValidResponse() throws Exception {
        var requestBuilder = get("/api/v1/chat/" + USER_2_USERNAME)
                .queryParam("page", "1")
                .queryParam("count", "2");

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("""
                        [{
                                "sender": {
                                    "username": "user1"
                                },
                                "recipient": {
                                    "username": "user2"
                                },
                                "text": "Third message from user1",
                                "sendingTime": "2023-08-19T15:11:00"
                            }, {
                                "sender": {
                                    "username": "user1"
                                },
                                "recipient": {
                                    "username": "user2"
                                },
                                "text": "Second message from user1",
                                "sendingTime": "2023-08-19T15:10:00"
                            }
                        ]
                        """, true)
        );
    }

    @WithMockUser(username = "user2")
    @Test
    void handleGetChat_EmptyChat_ReturnValidResponse() throws Exception {
        var requestBuilder = get("/api/v1/chat/" + USER_3_USERNAME);

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("""
                        [ ]
                        """)
        );
    }
}