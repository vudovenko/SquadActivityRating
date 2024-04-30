insert into security_users (login, password, active)
VALUES ('admin', '$2a$10$s7ECzeXM9/ztbWtY2wQxzeGTBKqe4z3GcQCzb3Q5k8wi.qUe9PTBi', true),
       ('admin1', '$2a$10$5WtlEFrB3IeMSiGXynMlLuKwaPOHydAWYCv2myPz2RjDkriVfayHS', true),
       ('user', '$2a$10$w3AaLOJdXj2jnm9w.tMizO0CfmyA6awCKmgJXSc.v/Uu/s8I.ENQS', true),
       ('user2', '$2a$10$ZeJ/.C5zDZOxrZwq28cZMeG.N0DtlBSna0o50y7hT3JD63V79Y7y2', true),
       ('user3', '$2a$10$BXuCcTZmPCSWtmxTdQbcG.G.axV0i72uZKZWsJJyaG9XbVJ5rQ1Wq', true),
       ('user4', '$2a$10$bauhh4OLWtLBwblLWvFYrOWq8QQ/dMFj/6brNbc5vQtof9kk9JLpS', true),
       ('user5', '$2a$10$PtC1hMP2VZHY9hlTjdLQSeYfJjgacLnhsOry6zhHfOZWT.QiD47Vu', true),
       ('user6', '$2a$10$AKROflPrgfgvqckpnKBR8uIeKHZHwFTjalxnn9SXBaI.Bo4CUHipG', true);

insert into user_role (user_id, roles)
VALUES (1, 'ADMIN'),
       (2, 'ADMIN'),
       (3, 'USER'),
       (4, 'USER'),
       (5, 'USER'),
       (6, 'USER'),
       (7, 'USER'),
       (8, 'USER');

insert into squad_users (security_id, squad_id, email, firstname, lastname, patronymic)
VALUES (1, null, 'admin@ru', 'Олег', 'Ромин', 'Петрович'),
       (2, null, 'admin1@com', 'Юлия', 'Чулкова', 'Тамерлановна');

insert into squads (commander_id, description, name)
VALUES (1, 'Отряд, любящий рыбалку', 'Рыбаки'),
       (2, 'Самураи Сибири', 'Самураи');

insert into squad_users (security_id, squad_id, email, firstname, lastname, patronymic)
VALUES (3, 1, 'user@ru', 'Константин', 'Чувалов', 'Александрович'),
       (4, 1, 'user2@ru', 'Александр', 'Кузнецов', 'Николаевич'),
       (5, 2, 'user3@ru', 'Алексей', 'Карелин', 'Генадьевич'),
       (6, 2, 'user4@ru', 'Анна', 'Тортолюбова', 'Артемовна'),
       (7, 2, 'user5@ru', 'Матвей', 'Куликов', 'Григорьевич'),
       (8, 2, 'user6@ru', 'Алла', 'Милославская', 'Петровна');

insert into membership_applications (squad_id, squad_user_id)
VALUES (1, 3),
       (2, 7),
       (2, 8);