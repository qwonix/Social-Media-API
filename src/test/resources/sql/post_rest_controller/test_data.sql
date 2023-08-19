insert into user_profile (id, username, email, password_hash, role)
values ('7cfb092f-e7df-497b-bcf3-f4c7c6ebb341', 'user1', 'user1@example.com',
        '$2a$12$kcNfKueCbal2UB.Eac2JlOOEUmPg9p/9acS8QpVd.0cXmoHiw2Lz.', 'USER'),
       ('1698f3cf-8751-48f9-9445-9d2d8ff2b062', 'user2', 'user2@example.com',
        '$2a$12$YplcA43IL7eYZajOWoJTRuw2DsFFDffXaX0sH7tC0.T.NBPvX6jZe', 'USER');

insert into post (id, user_id, title, text, created_at)
values ('ecad0472-f529-4daa-afde-cd539ebc9391', '7cfb092f-e7df-497b-bcf3-f4c7c6ebb341', 'Lorem ipsum',
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam non erat at velit fermentum semper.',
        '2023-08-19 12:00:00'),
       ('87dbb176-217f-4b68-b6c4-126affcc9a47', '1698f3cf-8751-48f9-9445-9d2d8ff2b062', 'Class aptent',
        'Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos.',
        '2023-08-19 13:00:00');