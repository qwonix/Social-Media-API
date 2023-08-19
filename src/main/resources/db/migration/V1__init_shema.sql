CREATE TABLE user_profile
(
    id            UUID PRIMARY KEY,
    username      VARCHAR(50) UNIQUE  NOT NULL,
    email         VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(100)        NOT NULL,
    role          VARCHAR(100)        NOT NULL
);

CREATE TABLE post
(
    id         UUID PRIMARY KEY,
    user_id    UUID REFERENCES user_profile NOT NULL,
    title      VARCHAR(100),
    text       TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE friend
(
    user_id   UUID REFERENCES user_profile,
    friend_id UUID REFERENCES user_profile,
    status    VARCHAR(100),
    PRIMARY KEY (user_id, friend_id)
);

CREATE TABLE message
(
    id           BIGSERIAL PRIMARY KEY,
    sender_id    UUID REFERENCES user_profile,
    recipient_id UUID REFERENCES user_profile,
    text         TEXT,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE image
(
    image_name VARCHAR(255) PRIMARY KEY,
    user_id    UUID REFERENCES user_profile NOT NULL
);

create table post_image
(
    image_name VARCHAR(255) REFERENCES image NOT NULL,
    post_id    UUID REFERENCES post NOT NULL,
    PRIMARY KEY (image_name, post_id)
);
