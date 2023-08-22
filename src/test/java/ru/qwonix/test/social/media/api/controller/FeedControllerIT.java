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

@Sql("/sql/feed_rest_controller/test_data.sql")
@SpringBootTest(classes = TestcontainersConfiguration.class)
@Transactional
@AutoConfigureMockMvc
class FeedControllerIT {

    @Autowired
    MockMvc mockMvc;

    @WithMockUser(username = "user1")
    @Test
    void handleGetFeed_WithoutParams_ReturnValidResponse() throws Exception {
        var requestBuilder = get("/api/v1/feed");

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("""
                        [{
                                "id": "d16f3596-150b-4136-8394-6195e0a85dca",
                                "title": "Suspendisse varius",
                                "text": "Suspendisse varius ac massa viverra imperdiet. Aliquam luctus ipsum at suscipit accumsan.",
                                "createdAt": "2023-08-19T14:00:00",
                                "images": [{
                                        "imageName": "640f34de-9daf-4b6e-8f53-8c6a777a9532_image_3.png"
                                    }
                                ],
                                "owner": {
                                    "username": "user3"
                                }
                            }, {
                                "id": "87dbb176-217f-4b68-b6c4-126affcc9a47",
                                "title": "Class aptent",
                                "text": "Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos.",
                                "createdAt": "2023-08-19T13:00:00",
                                "images": [{
                                        "imageName": "4f7a4e6e-2fa8-4152-9254-1a303bcea7ec_image_2.png"
                                    }
                                ],
                                "owner": {
                                    "username": "user2"
                                }
                            }, {
                                "id": "ecad0472-f529-4daa-afde-cd539ebc9391",
                                "title": "Lorem ipsum",
                                "text": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam non erat at velit fermentum semper.",
                                "createdAt": "2023-08-19T12:00:00",
                                "images": [{
                                        "imageName": "ca444eab-30e7-4bdc-ac1e-a2fe48db8f60_image_1.png"
                                    }
                                ],
                                "owner": {
                                    "username": "user2"
                                }
                            }
                        ]
                        """, true)
        );
    }

    @WithMockUser(username = "user2")
    @Test
    void handleGetFeed_OnePost_ReturnValidResponse() throws Exception {
        var requestBuilder = get("/api/v1/feed")
                .queryParam("page", "0")
                .queryParam("count", "10");

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("""
                        [ {
                                "id": "d16f3596-150b-4136-8394-6195e0a85dca",
                                "title": "Suspendisse varius",
                                "text": "Suspendisse varius ac massa viverra imperdiet. Aliquam luctus ipsum at suscipit accumsan.",
                                "createdAt": "2023-08-19T14:00:00",
                                "images": [{
                                        "imageName": "640f34de-9daf-4b6e-8f53-8c6a777a9532_image_3.png"
                                    }
                                ],
                                "owner": {
                                    "username": "user3"
                                }
                            }
                        ]
                        """, true)
        );
    }

    @WithMockUser(username = "user1")
    @Test
    void handleGetFeed_Page1Size2_ReturnValidResponse() throws Exception {
        var requestBuilder = get("/api/v1/feed")
                .queryParam("page", "1")
                .queryParam("count", "2");

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("""
                        [{
                                "id": "ecad0472-f529-4daa-afde-cd539ebc9391",
                                "title": "Lorem ipsum",
                                "text": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam non erat at velit fermentum semper.",
                                "createdAt": "2023-08-19T12:00:00",
                                "images": [{
                                        "imageName": "ca444eab-30e7-4bdc-ac1e-a2fe48db8f60_image_1.png"
                                    }
                                ],
                                "owner": {
                                    "username": "user2"
                                }
                            }
                        ]
                        """, true)
        );
    }

}