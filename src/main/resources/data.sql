insert into security_users (login, password, active)
VALUES ('commander1', '$2a$10$s7ECzeXM9/ztbWtY2wQxzeGTBKqe4z3GcQCzb3Q5k8wi.qUe9PTBi', true),
       ('commander2', '$2a$10$5WtlEFrB3IeMSiGXynMlLuKwaPOHydAWYCv2myPz2RjDkriVfayHS', true),
       ('commander3', '$2a$10$5WtlEFrB3IeMSiGXynMlLuKwaPOHydAWYCv2myPz2RjDkriVfayHS', true),
       ('fighter1', '$2a$10$w3AaLOJdXj2jnm9w.tMizO0CfmyA6awCKmgJXSc.v/Uu/s8I.ENQS', true),
       ('fighter2', '$2a$10$ZeJ/.C5zDZOxrZwq28cZMeG.N0DtlBSna0o50y7hT3JD63V79Y7y2', true),
       ('fighter3', '$2a$10$BXuCcTZmPCSWtmxTdQbcG.G.axV0i72uZKZWsJJyaG9XbVJ5rQ1Wq', true),
       ('fighter4', '$2a$10$bauhh4OLWtLBwblLWvFYrOWq8QQ/dMFj/6brNbc5vQtof9kk9JLpS', true),
       ('fighter5', '$2a$10$PtC1hMP2VZHY9hlTjdLQSeYfJjgacLnhsOry6zhHfOZWT.QiD47Vu', true),
       ('fighter6', '$2a$10$AKROflPrgfgvqckpnKBR8uIeKHZHwFTjalxnn9SXBaI.Bo4CUHipG', true),
       ('fighter7', '$2a$10$XVpQWnLXe1aXbQ0iEYqG3eX9XU8b6bY4bJ5bIiZn4wI4bIiZn4wIi', true),
       ('fighter8', '$2a$10$XVpQWnLXe1aXbQ0iEYqG3eX9XU8b6bY4bJ5bIiZn4wI4bIiZn4wIi', true),
       ('fighter9', '$2a$10$XVpQWnLXe1aXbQ0iEYqG3eX9XU8b6bY4bJ5bIiZn4wI4bIiZn4wIi', true),
       ('fighter10', '$2a$10$XVpQWnLXe1aXbQ0iEYqG3eX9XU8b6bY4bJ5bIiZn4wI4bIiZn4wIi', true),
       ('fighter11', '$2a$10$XVpQWnLXe1aXbQ0iEYqG3eX9XU8b6bY4bJ5bIiZn4wI4bIiZn4wIi', true);

insert into user_role (user_id, roles)
VALUES (1, 'COMMANDER'),
       (2, 'COMMANDER'),
       (3, 'COMMANDER'),
       (4, 'FIGHTER'),
       (5, 'FIGHTER'),
       (6, 'FIGHTER'),
       (7, 'FIGHTER'),
       (8, 'FIGHTER'),
       (9, 'FIGHTER'),
       (10, 'FIGHTER'),
       (11, 'FIGHTER'),
       (12, 'FIGHTER'),
       (13, 'FIGHTER'),
       (14, 'FIGHTER');

insert into squad_users (security_id, squad_id, email, firstname, lastname, patronymic)
VALUES (1, null, 'commander1@ru', 'Олег', 'Ромин', 'Петрович'),
       (2, null, 'commander2@com', 'Юлия', 'Чулкова', 'Тамерлановна');

insert into squads (commander_id, description, name)
VALUES (1, 'Отряд, любящий рыбалку', 'Рыбаки'),
       (2, 'Самураи Сибири', 'Самураи'),
       (null, 'Тестовый отряд', 'Тестовый отряд');

insert into squad_users (security_id, squad_id, email, firstname, lastname, patronymic)
VALUES (3, null, 'commander3@ru', 'Александра', 'Печеркина', 'Евгениевна'),
       (4, 1, 'fighter1@ru', 'Константин', 'Чувалов', 'Александрович'),
       (5, 1, 'fighter2@ru', 'Александр', 'Кузнецов', 'Николаевич'),
       (6, 2, 'fighter3@ru', 'Алексей', 'Карелин', 'Генадьевич'),
       (7, 2, 'fighter4@ru', 'Анна', 'Тортолюбова', 'Артемовна'),
       (8, 2, 'fighter5@ru', 'Матвей', 'Куликов', 'Григорьевич'),
       (9, null, 'fighter6@ru', 'Алла', 'Милославская', 'Петровна'),
       (10, null, 'fighter7@ru', 'Юрий', 'Громов', 'Леонидович'),
       (11, null, 'fighter8@ru', 'Алексей', 'Петров', 'Александрович'),
       (12, null, 'fighter9@ru', 'Игнат', 'Смирнов', 'Вадимович'),
       (13, null, 'fighter10@ru', 'Людмила', 'Морозова', 'Михайловна'),
       (14, null, 'fighter11@ru', 'Михаил', 'Васильев', 'Александрович');

insert into membership_applications (squad_id, squad_user_id)
VALUES (1, 12),
       (2, 13),
       (2, 14);