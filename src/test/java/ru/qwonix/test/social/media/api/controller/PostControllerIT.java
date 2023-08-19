package ru.qwonix.test.social.media.api.controller;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import ru.qwonix.test.social.media.api.TestcontainersConfiguration;
import ru.qwonix.test.social.media.api.entity.Post;
import ru.qwonix.test.social.media.api.entity.Role;
import ru.qwonix.test.social.media.api.entity.UserProfile;
import ru.qwonix.test.social.media.api.repository.PostRepository;
import ru.qwonix.test.social.media.api.repository.UserProfileRepository;
import ru.qwonix.test.social.media.api.serivce.AuthenticationService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = TestcontainersConfiguration.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PostControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserProfileRepository userProfileRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    private String token;

    @BeforeEach
    void setUpData() {
        var user1 = userProfileRepository.save(UserProfile.builder()
                .username("user1")
                .email("user1@example.com")
                .passwordHash(passwordEncoder.encode("password1"))
                .role(Role.USER)
                .build());

        var user2 = userProfileRepository.save(UserProfile.builder()
                .username("user2")
                .email("user2@example.com")
                .passwordHash(passwordEncoder.encode("password2"))
                .role(Role.USER)
                .build());

        postRepository.save(Post.builder()
                .id(1L)
                .title("Lorem ipsum")
                .text("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam non erat at velit fermentum semper.")
                .user(user1)
                .build());

        postRepository.save(Post.builder()
                .id(2L)
                .title("Class aptent")
                .text("Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos.")
                .user(user2)
                .build());
    }

    @BeforeEach
    void setUpToken() {
        token = authenticationService.generateToken("user1", Role.USER.getAuthorities());

    }

    @Test
    void handleGet_ReturnValidResponse() throws Exception {
        var requestBuilder = get("/api/v1/post/1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("""
                        {
                          "id": 1,
                          "title": "Lorem ipsum",
                          "text": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam non erat at velit fermentum semper.",
                          "owner": {
                            "username": "user1"
                          }
                        }
                        """)
        );
    }

    @Test
    void handleGet_OtherUsersPost_ReturnValidResponse() throws Exception {
        var requestBuilder = get("/api/v1/post/2")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("""
                        {
                          "id": 2,
                          "title": "Class aptent",
                          "text": "Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos.",
                          "owner": {
                            "username": "user2"
                          }
                        }
                        """)
        );
    }

    @Test
    void handleGet_IdIsInvalid_ReturnNotFound() throws Exception {
        var requestBuilder = get("/api/v1/post/100")
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
                             "id": 3,
                             "title": "New Title",
                             "text": "New Text about Spring",
                             "createdAt": null,
                             "owner": {
                                 "username": "user1"
                             }
                         }
                        """);


        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isCreated(),
                header().exists(HttpHeaders.LOCATION),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("""
                        {
                            "id": 3,
                            "title": "New Title",
                            "text": "New Text about Spring",
                            "owner": {
                                "username": "user1"
                            }
                        }
                        """)
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
        var requestBuilder = patch("/api/v1/post/1")
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
                            "id": 1,
                            "title": "New Title For 1",
                            "text": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam non erat at velit fermentum semper.",
                            "owner": {
                                "username": "user1"
                            }
                        }
                                                
                        """)
        );
    }

    @Test
    void handleUpdate_TitleIsTooLong_ReturnErrorMessage() throws Exception {
        var requestBuilder = patch("/api/v1/post/1")
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
        var requestBuilder = patch("/api/v1/post/1")
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
                            "id": 1,
                            "title": "Lorem ipsum",
                            "text": "New Text For 1",
                            "owner": {
                                "username": "user1"
                            }
                        }
                        """)
        );
    }

    @Test
    void handleUpdate_NewTitleAndText_ReturnValidResponse() throws Exception {
        var requestBuilder = patch("/api/v1/post/1")
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
                          "id": 1,
                          "title": "New Title For 1",
                          "text": "New Text For 1",
                          "owner": {
                            "username": "user1"
                          }
                        }
                        """)
        );
    }

    @Test
    void handleUpdate_OtherUsersPost_ReturnErrorMessage() throws Exception {
        var requestBuilder = patch("/api/v1/post/2")
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
        var requestBuilder = delete("/api/v1/post/1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk()
        );
    }

    @Test
    void handleDelete_OtherUsersPost_ReturnErrorMessage() throws Exception {
        var requestBuilder = delete("/api/v1/post/2")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isForbidden()
        );
    }
}