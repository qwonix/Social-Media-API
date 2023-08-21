insert into user_profile (id, username, email, password_hash, role)
values ('7cfb092f-e7df-497b-bcf3-f4c7c6ebb341', 'user1', 'user1@example.com',
        '$2a$12$kcNfKueCbal2UB.Eac2JlOOEUmPg9p/9acS8QpVd.0cXmoHiw2Lz.', 'USER');

insert into image (image_name, user_id)
values ('ca444eab-30e7-4bdc-ac1e-a2fe48db8f60_image_1.png', '7cfb092f-e7df-497b-bcf3-f4c7c6ebb341');