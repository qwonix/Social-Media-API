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

    @WithMockUser(username = "user1")
    @Test
    void handleGet_ReturnValidResponse() throws Exception {
        var requestBuilder = get("/api/v1/post/" + POST_1_ID);

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("""
                        {
                            "id": "%s",
                            "title": "Lorem ipsum",
                            "text": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam non erat at velit fermentum semper.",
                            "createdAt": "2023-08-19T12:00:00",
                            "images": [{
                                    "imageName": "ca444eab-30e7-4bdc-ac1e-a2fe48db8f60_image_1.png"
                                }
                            ],
                            "owner": {
                                "username": "user1"
                            }
                        }
                        """.formatted(POST_1_ID))
        );
    }

    @WithMockUser(username = "user1")
    @Test
    void handleGet_AnotherUsersPost_ReturnValidResponse() throws Exception {
        var requestBuilder = get("/api/v1/post/" + POST_2_ID);

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("""
                        {
                            "id": "%s",
                            "title": "Class aptent",
                            "text": "Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos.",
                            "createdAt": "2023-08-19T13:00:00",
                            "images": [ ],
                            "owner": {
                                "username": "user2"
                            }
                        }
                        """.formatted(POST_2_ID))
        );
    }

    @WithMockUser(username = "user1")
    @Test
    void handleGet_IdIsInvalid_ReturnNotFound() throws Exception {
        var requestBuilder = get("/api/v1/post/f3ec24d6-0597-4241-af31-ea524c65c333");

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound()
        );
    }

    @WithMockUser(username = "user1")
    @Test
    void handleCreate_DataIsValid_ReturnValidResponse() throws Exception {
        var requestBuilder = post("/api/v1/post")
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
                jsonPath("$.images").isEmpty(),
                jsonPath("$.owner.username").value("user1")
        );
    }

    @WithMockUser(username = "user1")
    @Test
    void handleCreate_TitleIsTooLong_ReturnErrorMessage() throws Exception {
        var requestBuilder = post("/api/v1/post")
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

    @WithMockUser(username = "user1")
    @Test
    void handleUpdate_NewTitle_ReturnValidResponse() throws Exception {
        var requestBuilder = patch("/api/v1/post/" + POST_1_ID)
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
                            "images": [{
                                    "imageName": "ca444eab-30e7-4bdc-ac1e-a2fe48db8f60_image_1.png"
                                }
                            ],
                            "owner": {
                                "username": "user1"
                            }
                        }
                        """.formatted(POST_1_ID))
        );
    }

    @WithMockUser(username = "user1")
    @Test
    void handleUpdate_TitleIsTooLong_ReturnErrorMessage() throws Exception {
        var requestBuilder = patch("/api/v1/post/" + POST_1_ID)
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

    @WithMockUser(username = "user1")
    @Test
    void handleUpdate_NewText_ReturnValidResponse() throws Exception {
        var requestBuilder = patch("/api/v1/post/" + POST_1_ID)
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
                            "images": [{
                                    "imageName": "ca444eab-30e7-4bdc-ac1e-a2fe48db8f60_image_1.png"
                                }
                            ],
                            "owner": {
                                "username": "user1"
                            }
                        }
                        """.formatted(POST_1_ID))
        );
    }

    @WithMockUser(username = "user1")
    @Test
    void handleUpdate_NewTitleAndText_ReturnValidResponse() throws Exception {
        var requestBuilder = patch("/api/v1/post/" + POST_1_ID)
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
                             "images": [{
                                    "imageName": "ca444eab-30e7-4bdc-ac1e-a2fe48db8f60_image_1.png"
                                }
                             ],
                             "owner": {
                                 "username": "user1"
                             }
                        }
                        """.formatted(POST_1_ID))

        );
    }

    @WithMockUser(username = "user1")
    @Test
    void handleUpdate_AnotherUsersPost_ReturnErrorMessage() throws Exception {
        var requestBuilder = patch("/api/v1/post/" + POST_2_ID)
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

    @WithMockUser(username = "user1")
    @Test
    void handleDelete_ReturnSuccess() throws Exception {
        var requestBuilder = delete("/api/v1/post/" + POST_1_ID);

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk()
        );
    }

    @WithMockUser(username = "user1")
    @Test
    void handleDelete_AnotherUsersPost_ReturnErrorMessage() throws Exception {
        var requestBuilder = delete("/api/v1/post/" + POST_2_ID);

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isForbidden()
        );
    }


    @WithMockUser(username = "user1")
    @Test
    void handleAttachImage__Success() throws Exception {
        var requestBuilder = post("/api/v1/post/" + POST_1_ID + "/image")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "imageName" : "4f7a4e6e-2fa8-4152-9254-1a303bcea7ec_image_2.png"
                        }
                        """);

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isCreated(),
                header().exists(HttpHeaders.LOCATION),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("""
                        {
                            "id": "ecad0472-f529-4daa-afde-cd539ebc9391",
                            "title": "Lorem ipsum",
                            "text": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam non erat at velit fermentum semper.",
                            "createdAt": "2023-08-19T12:00:00",
                            "images": [{
                                    "imageName": "ca444eab-30e7-4bdc-ac1e-a2fe48db8f60_image_1.png"
                                }, {
                                    "imageName": "4f7a4e6e-2fa8-4152-9254-1a303bcea7ec_image_2.png"
                                }
                            ],
                            "owner": {
                                "username": "user1"
                            }
                        }
                        """)
        );
    }


    @WithMockUser(username = "user1")
    @Test
    void handleAttachImage_ImageAlreadyAttached_ReturnErrorMessage() throws Exception {
        var requestBuilder = post("/api/v1/post/" + POST_1_ID + "/image")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "imageName" : "ca444eab-30e7-4bdc-ac1e-a2fe48db8f60_image_1.png"
                        }
                        """);

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("""
                        {
                            "errorMessages": [{
                                    "field": "imageName",
                                    "message": "The image has already been attached to the post"
                                }
                            ]
                        }
                        """)
        );
    }

    @WithMockUser(username = "user1")
    @Test
    void handleAttachImage_ImageDoseNotExists_ReturnErrorMessage() throws Exception {
        var requestBuilder = post("/api/v1/post/" + POST_1_ID + "/image")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "imageName" : "f695d9b8-8905-47e9-8779-483de28fd411_image_4.png"
                        }
                        """);

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("""
                        {
                            "errorMessages": [{
                                    "field": "imageName",
                                    "message": "Image does not exist. In order to attach an image, you need to upload it"
                                }
                            ]
                        }
                        """)
        );
    }

    @WithMockUser(username = "user1")
    @Test
    void handleAttachImage_PostDoseNotExists_ReturnErrorMessage() throws Exception {
        final var POST_ID = "65955315-735b-4d87-81a9-cc48e1ed638b";
        var requestBuilder = post("/api/v1/post/" + POST_ID + "/image")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "imageName" : "f695d9b8-8905-47e9-8779-483de28fd411_image_3.png"
                        }
                        """);

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound()
        );
    }


    @WithMockUser(username = "user1")
    @Test
    void handleAttachImage_ImageBelongsToAnotherUser_ReturnErrorMessage() throws Exception {
        var requestBuilder = post("/api/v1/post/" + POST_1_ID + "/image")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "imageName" : "640f34de-9daf-4b6e-8f53-8c6a777a9532_image_3.png"
                        }
                        """);

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isForbidden()
        );
    }


    @WithMockUser(username = "user1")
    @Test
    void handleDetachImage__Success() throws Exception {
        var requestBuilder = delete("/api/v1/post/" + POST_1_ID + "/image")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "imageName" : "ca444eab-30e7-4bdc-ac1e-a2fe48db8f60_image_1.png"
                        }
                        """);

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("""
                        {
                            "id": "ecad0472-f529-4daa-afde-cd539ebc9391",
                            "title": "Lorem ipsum",
                            "text": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam non erat at velit fermentum semper.",
                            "createdAt": "2023-08-19T12:00:00",
                            "images": [ ],
                            "owner": {
                                "username": "user1"
                            }
                        }
                        """)
        );
    }


    @WithMockUser(username = "user1")
    @Test
    void handleDetachImage_ImageNotAttached_ReturnErrorMessage() throws Exception {
        var requestBuilder = delete("/api/v1/post/" + POST_1_ID + "/image")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "imageName" : "4f7a4e6e-2fa8-4152-9254-1a303bcea7ec_image_2.png"
                        }
                        """);

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("""
                        {
                            "errorMessages": [{
                                    "field": "imageName",
                                    "message": "The image is not attached to the post"
                                }
                            ]
                        }
                        """)
        );
    }

    @WithMockUser(username = "user1")
    @Test
    void handleDetachImage_ImageDoseNotExists_ReturnErrorMessage() throws Exception {
        var requestBuilder = delete("/api/v1/post/" + POST_1_ID + "/image")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "imageName" : "f695d9b8-8905-47e9-8779-483de28fd411_image_4.png"
                        }
                        """);

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("""
                        {
                            "errorMessages": [{
                                    "field": "imageName",
                                    "message": "Image does not exist. In order to detach an image, you need to upload and attach it"
                                }
                            ]
                        }
                        """)
        );
    }

    @WithMockUser(username = "user1")
    @Test
    void handleDetachImage_PostDoseNotExists_ReturnErrorMessage() throws Exception {
        final var POST_ID = "65955315-735b-4d87-81a9-cc48e1ed638b";
        var requestBuilder = delete("/api/v1/post/" + POST_ID + "/image")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "imageName" : "f695d9b8-8905-47e9-8779-483de28fd411_image_3.png"
                        }
                        """);

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound()
        );
    }

    @WithMockUser(username = "user1")
    @Test
    void handleDetachImage_ImageBelongsToAnotherPost_ReturnErrorMessage() throws Exception {
        var requestBuilder = delete("/api/v1/post/" + POST_2_ID + "/image")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "imageName" : "640f34de-9daf-4b6e-8f53-8c6a777a9532_image_3.png"
                        }
                        """);

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isForbidden()
        );
    }
}