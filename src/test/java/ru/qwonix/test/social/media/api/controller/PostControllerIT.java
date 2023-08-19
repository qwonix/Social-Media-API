package ru.qwonix.test.social.media.api.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.qwonix.test.social.media.api.TestcontainersConfiguration;
import ru.qwonix.test.social.media.api.entity.Role;
import ru.qwonix.test.social.media.api.serivce.AuthenticationService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql("/sql/post_rest_controller/test_data.sql")
@Transactional
@SpringBootTest(classes = TestcontainersConfiguration.class)
@AutoConfigureMockMvc
class PostControllerIT {

    private static final String POST_1_ID = "ecad0472-f529-4daa-afde-cd539ebc9391";
    private static final String POST_2_ID = "87dbb176-217f-4b68-b6c4-126affcc9a47";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    PasswordEncoder passwordEncoder;

    private String token;

    @BeforeEach
    void setUpToken() {
        token = authenticationService.generateToken("user1", Role.USER.getAuthorities());
    }

    @Test
    void handleGet_ReturnValidResponse() throws Exception {
        var requestBuilder = get("/api/v1/post/" + POST_1_ID)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("""
                        {
                          "id": "%s",
                          "title": "Lorem ipsum",
                          "text": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam non erat at velit fermentum semper.",
                          "createdAt" : "2023-08-19T12:00:00",
                          "owner": {
                            "username": "user1"
                          }
                        }
                        """.formatted(POST_1_ID))
        );
    }

    @Test
    void handleGet_OtherUsersPost_ReturnValidResponse() throws Exception {
        var requestBuilder = get("/api/v1/post/" + POST_2_ID)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("""
                        {
                          "id": %s,
                          "title": "Class aptent",
                          "text": "Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos.",
                          "createdAt" : "2023-08-19T13:00:00",
                          "owner": {
                            "username": "user2"
                          }
                        }
                        """.formatted(POST_2_ID))
        );
    }

    @Test
    void handleGet_IdIsInvalid_ReturnNotFound() throws Exception {
        var requestBuilder = get("/api/v1/post/f3ec24d6-0597-4241-af31-ea524c65c333")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound()
        );
    }

    @Test
    void handleCreate_DataIsValid_ReturnValidResponse() throws Exception {
        var requestBuilder = post("/api/v1/post")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                             "title": "New Title",
                             "text": "New Text about Spring"
                         }
                        """);


        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isCreated(),
                header().exists(HttpHeaders.LOCATION),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.id").isString(),
                jsonPath("$.title").value("New Title"),
                jsonPath("$.text").value("New Text about Spring"),
                jsonPath("$.createdAt").isString(),
                jsonPath("$.owner.username").value("user1")
        );
    }

    @Test
    void handleCreate_TitleIsTooLong_ReturnErrorMessage() throws Exception {
        var requestBuilder = post("/api/v1/post")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "title" : "New Loooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooong Title",
                            "text" : "New Text about Spring"
                        }
                        """);


        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest(),
                header().doesNotExist(HttpHeaders.LOCATION),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("""
                        {
                            "errorMessages": [{
                                    "field": "title",
                                    "message": "title must be shorter than 100 characters"
                                }
                            ]
                        }
                        """)
        );
    }

    @Test
    void handleUpdate_NewTitle_ReturnValidResponse() throws Exception {
        var requestBuilder = patch("/api/v1/post/" + POST_1_ID)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "title" : "New Title For 1"
                        }
                        """);


        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("""
                        {
                            "id": "%s",
                            "title": "New Title For 1",
                            "text": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam non erat at velit fermentum semper.",
                            "createdAt" : "2023-08-19T12:00:00",
                            "owner": {
                                "username": "user1"
                            }
                        }
                        """.formatted(POST_1_ID))
        );
    }

    @Test
    void handleUpdate_TitleIsTooLong_ReturnErrorMessage() throws Exception {
        var requestBuilder = patch("/api/v1/post/" + POST_1_ID)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "title" : "New Loooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooong Title"
                        }
                        """);


        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("""
                        {
                          "errorMessages": [
                            {
                              "field": "title",
                              "message": "title must be shorter than 100 characters"
                            }
                          ]
                        }
                        """)
        );
    }

    @Test
    void handleUpdate_NewText_ReturnValidResponse() throws Exception {
        var requestBuilder = patch("/api/v1/post/" + POST_1_ID)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "text" : "New Text For 1"
                        }
                        """);


        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("""
                        {
                            "id": "%s",
                            "title": "Lorem ipsum",
                            "text": "New Text For 1",
                            "createdAt" : "2023-08-19T12:00:00",
                            "owner": {
                                "username": "user1"
                            }
                        }
                        """.formatted(POST_1_ID))
        );
    }

    @Test
    void handleUpdate_NewTitleAndText_ReturnValidResponse() throws Exception {
        var requestBuilder = patch("/api/v1/post/" + POST_1_ID)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "title" : "New Title For 1",
                            "text" : "New Text For 1"
                        }
                        """);


        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("""
                        {
                             "id": "%s",
                             "title": "New Title For 1",
                             "text": "New Text For 1",
                             "createdAt" : "2023-08-19T12:00:00",
                             "owner": {
                                 "username": "user1"
                             }
                        }
                        """.formatted(POST_1_ID))

        );
    }

    @Test
    void handleUpdate_OtherUsersPost_ReturnErrorMessage() throws Exception {
        var requestBuilder = patch("/api/v1/post/" + POST_2_ID)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "title" : "New Title For 2",
                            "text" : "New Text For 2"
                        }
                        """);


        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isForbidden()
        );
    }

    @Test
    void handleDelete_ReturnSuccess() throws Exception {
        var requestBuilder = delete("/api/v1/post/" + POST_1_ID)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk()
        );
    }

    @Test
    void handleDelete_OtherUsersPost_ReturnErrorMessage() throws Exception {
        var requestBuilder = delete("/api/v1/post/" + POST_2_ID)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isForbidden()
        );
    }
}