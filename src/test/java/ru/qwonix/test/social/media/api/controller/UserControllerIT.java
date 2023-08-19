package ru.qwonix.test.social.media.api.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.qwonix.test.social.media.api.TestcontainersConfiguration;
import ru.qwonix.test.social.media.api.entity.Role;
import ru.qwonix.test.social.media.api.serivce.AuthenticationService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
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

    @Autowired
    AuthenticationService authenticationService;

    @Test
    void handleGet_BasicAuthenticationGetItself_ReturnFullUserProfile() throws Exception {
        var requestBuilder = get("/api/v1/user/profile/user1")
                .with(httpBasic("user1", "password1"));

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isUnauthorized()
        );
    }


    @Test
    void handleGet_TokenAuthenticationGetItself_ReturnFullUserProfile() throws Exception {
        final var user1 = "user1";
        var token = authenticationService.generateToken(user1, Role.USER.getAuthorities());

        var requestBuilder = get("/api/v1/user/profile/user1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

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

    @Test
    void handleGet_TokenAuthenticationGetOtherUser_ReturnPublicUserProfile() throws Exception {
        final var user1 = "user1";
        var token = authenticationService.generateToken(user1, Role.USER.getAuthorities());

        var requestBuilder = get("/api/v1/user/profile/user2")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

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
    void handleGet_NoAuthentication_ReturnFullUserProfile() throws Exception {
        var requestBuilder = get("/api/v1/user/profile/user1");

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isUnauthorized()
        );
    }


}