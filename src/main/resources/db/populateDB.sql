
DELETE FROM meals;
DELETE FROM user_roles;
DELETE FROM users;

ALTER SEQUENCE global_seq_user RESTART WITH 100000;
ALTER SEQUENCE global_seq_meal RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES ('User', 'user@yandex.ru', 'password');


INSERT INTO users (name, email, password) VALUES ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES ('ROLE_USER', 100000), ('ROLE_ADMIN', 100001);

INSERT INTO meals(user_id, date_time) VALUES (100000, '2016-10-30 12:50');
INSERT INTO meals(user_id, date_time) VALUES (100000, '2016-10-30 18:00');
INSERT INTO meals(user_id, date_time) VALUES (100000, '2016-10-30 20:00');
INSERT INTO meals(user_id, date_time) VALUES (100001, '2016-10-31 11:00');
INSERT INTO meals(user_id, date_time) VALUES (100001, '2016-10-31 15:30');
INSERT INTO meals(user_id, date_time) VALUES (100001, '2016-10-31 21:40');