--Insert

INSERT INTO users VALUES (1, 'sasha', 'md593d1079655679037dd4ec5caf1b8e91a');
INSERT INTO users VALUES (2, 'lena', 'md593d1079655679037dd4ec5caf1b8e91a');

INSERT INTO roles VALUES (1, 'ROLE_ADMIN');
INSERT INTO roles VALUES (2, 'ROLE_USER');

INSERT INTO user_roles VALUES (1, 1);
INSERT INTO user_roles VALUES (1, 2);
INSERT INTO user_roles VALUES (2, 2);