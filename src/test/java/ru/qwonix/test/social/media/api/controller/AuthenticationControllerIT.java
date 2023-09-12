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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql("/sql/authentication_rest_controller/test_data.sql")
@SpringBootTest(classes = TestcontainersConfiguration.class)
@Transactional
@AutoConfigureMockMvc
class AuthenticationControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Test
    void handleGenerateAuthenticationToken_BasicAuthentication_ReturnsAuthenticationToken() throws Exception {
        var requestBuilder = get("/api/v1/auth")
                .with(httpBasic("user1", "password1"));

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.token").exists()
        );
    }

    @Test
    void handleGenerateAuthenticationToken_WrongPassword_ReturnsValidErrorResponse() throws Exception {
        var requestBuilder = get("/api/v1/auth")
                .with(httpBasic("user1", "password2"));

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isUnauthorized()
        );
    }

    @Test
    void handleRegisterNewUser_ReturnsSuccess() throws Exception {
        var requestBuilder = post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "username": "user2",
                            "email": "user2@example.com",
                            "password": "password2"
                        }
                        """);

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isCreated(),
                header().exists(HttpHeaders.LOCATION),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.id").isString(),
                jsonPath("$.username").value("user2"),
                jsonPath("$.email").value("user2@example.com")
        );
    }

    @Test
    void handleRegisterNewUser_DuplicateUsername_ReturnsValidErrorResponse() throws Exception {
        var requestBuilder = post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "username": "user1",
                            "email": "user2@example.com",
                            "password": "password2"
                        }
                        """);

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isConflict(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("""
                        {
                          "errorMessages": [
                            {
                              "field": "username",
                              "message": "username already exists"
                            }
                          ]
                        }
                        """)
        );
    }

    @Test
    void handleRegisterNewUser_DuplicateEmail_ReturnsValidErrorResponse() throws Exception {
        var requestBuilder = post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "username": "user2",
                            "email": "user1@example.com",
                            "password": "password2"
                        }
                        """);

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isConflict(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("""
                        {
                          "errorMessages": [
                            {
                              "field": "email",
                              "message": "email already exists"
                            }
                          ]
                        }
                        """)
        );
    }


    @Test
    void handleRegisterNewUser_EmailValidationError_ReturnsValidErrorResponse() throws Exception {
        var requestBuilder = post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "username": "user2",
                            "email": "user2example.com",
                            "password": "password2"
                        }
                        """);

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("""
                        {
                           "errorMessages": [
                             {
                               "field": "email",
                               "message": "must be a well-formed email address"
                             }
                           ]
                        }
                        """)
        );
    }

    @Test
    void handleRegisterNewUser_UsernameIsTooShort_ReturnsValidErrorResponse() throws Exception {
        var requestBuilder = post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "username": "u",
                            "email": "user2@example.com",
                            "password": "password2"
                        }
                        """);

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("""
                        {
                           "errorMessages": [
                             {
                               "field": "username",
                               "message": "username must be longer than 3 and shorter than 20 characters"
                             }
                           ]
                         }
                        """)
        );
    }

    @Test
    void handleRegisterNewUser_UsernameIsTooLong_ReturnsValidErrorResponse() throws Exception {
        var requestBuilder = post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "username": "uuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuser",
                            "email": "user2@example.com",
                            "password": "password2"
                        }
                        """);

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("""
                        {
                           "errorMessages": [
                             {
                               "field": "username",
                               "message": "username must be longer than 3 and shorter than 20 characters"
                             }
                           ]
                        }
                        """)
        );
    }

    @Test
    void handleRegisterNewUser_PasswordIsTooShort_ReturnsValidErrorResponse() throws Exception {
        var requestBuilder = post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "username": "user2",
                            "email": "user2@example.com",
                            "password": "2"
                        }
                        """);

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("""
                        {
                           "errorMessages": [
                             {
                               "field": "password",
                               "message": "password should have at least 8 characters"
                             }
                           ]
                        }
                        """)
        );
    }
}