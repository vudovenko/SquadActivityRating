insert into security_users (login, password, active)
VALUES ('commander1', '$2a$10$s7ECzeXM9/ztbWtY2wQxzeGTBKqe4z3GcQCzb3Q5k8wi.qUe9PTBi', true),
       ('commander2', '$2a$10$5WtlEFrB3IeMSiGXynMlLuKwaPOHydAWYCv2myPz2RjDkriVfayHS', true),
       ('commander3', '$2a$10$8Ptjbpe0.6BLsOk9dqgQA.Ye.SQpGD5xD.xi0t3bk9wggnChHTcTe', true),
       ('fighter1', '$2a$10$muWfb/PtVF3E8j.kJXDZRufATwed6JE2HNXP8c6d1uiVCOKgwrPZG', true),
       ('fighter2', '$2a$10$8hynLBRgAOU9OAnLFK6Q9OfH4zjztiSD6bsFK7i85LSKb1PKhF8Ru', true),
       ('fighter3', '$2a$10$R/iW/MViuD5.vEN88xLpL.kQ8cWw.eBMSO0e89cVDkIn4VqIiiHiu', true),
       ('fighter4', '$2a$10$GUBsdxiTVDRD/v0tzaOkFuHdupscULGejCqLbgJKGJMGMAzGTtIYi', true),
       ('fighter5', '$2a$10$ZibNoERw5HksGwiu1zxHMOndH2EzY0SRg7TJ4BUfVkLCJPGc2NU1S', true),
       ('fighter6', '$2a$10$JoMEgNH1Hka2sZthwhkRu.QtbwBbeFRP56E5DUTiM7/K0190y9UNm', true),
       ('fighter7', '$2a$10$tIsfeLi7iPDUxP2C9n3CfebA3diJ1R42vRvs5NSgVv7Ky9KTm.l7i', true),
       ('fighter8', '$2a$10$VW56a6ey.ilowYi3w3SfxuePGgKXUwFu8sz848Bkz2kciIY6qH8J6', true),
       ('fighter9', '$2a$10$OkEzgXG7dG0AbKCWtEnpsunDisF9HAKJ3fcXE23CONI6ulE80BE8i', true),
       ('fighter10', '$2a$10$gHiN4BEi8v8EW/42H7w3w.qiLKQ9WKhrp/0RScSbESSvXiYf7HAAm', true),
       ('fighter11', '$2a$10$eu2mo7AHG7U.Ey53XevYhuZNuY9kOsFnf/ZtoGC3UM.MWZweMXzw.', true);

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