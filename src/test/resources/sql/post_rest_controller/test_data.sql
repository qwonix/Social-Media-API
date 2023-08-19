insert into user_profile (id, username, email, password_hash, role)
values ('7cfb092f-e7df-497b-bcf3-f4c7c6ebb341', 'user1', 'user1@example.com',
        '$2a$12$kcNfKueCbal2UB.Eac2JlOOEUmPg9p/9acS8QpVd.0cXmoHiw2Lz.', 'USER'),
       ('1698f3cf-8751-48f9-9445-9d2d8ff2b062', 'user2', 'user2@example.com',
        '$2a$12$YplcA43IL7eYZajOWoJTRuw2DsFFDffXaX0sH7tC0.T.NBPvX6jZe', 'USER');

insert into post (post_id, user_id, title, text, created_at)
values (1, '7cfb092f-e7df-497b-bcf3-f4c7c6ebb341', 'Lorem ipsum',
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam non erat at velit fermentum semper.',
        '2023-08-19 12:00:00'),
       (2, '7cfb092f-e7df-497b-bcf3-f4c7c6ebb341', 'Class aptent',
        'Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos.',
        '2023-08-19 13:00:00'),
       (3, '1698f3cf-8751-48f9-9445-9d2d8ff2b062', 'Nullam a venenatis risus',
        'Nullam a venenatis risus. Sed quis orci in arcu tempor ullamcorper.',
        '2023-08-19 14:00:00');