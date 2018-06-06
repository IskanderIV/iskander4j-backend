CREATE ROLE clever_admin LOGIN
  ENCRYPTED PASSWORD 'md593d1079655679037dd4ec5caf1b8e91a'
  NOSUPERUSER INHERIT CREATEDB NOCREATEROLE NOREPLICATION;

-- Table: users

CREATE TABLE IF NOT EXISTS users (
  id int NOT NULL UNIQUE PRIMARY KEY,
  username varchar(255) NOT NULL,
  password varchar(255) NOT NULL,
  CONSTRAINT voidUserName CHECK (username <> '')
);

-- Table: roles

CREATE TABLE IF NOT EXISTS roles (
  id int NOT NULL UNIQUE PRIMARY KEY,
  rolename varchar(100) NOT NULL
);

-- Table for mapping users and roles: user_roles

CREATE TABLE IF NOT EXISTS user_roles (
  user_id int NOT NULL,
  role_id int NOT NULL,

  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (role_id) REFERENCES roles(id),

  UNIQUE (user_id, role_id)
);

-- Table: boards

CREATE TABLE IF NOT EXISTS boards (
  id int NOT NULL UNIQUE PRIMARY KEY,
  boardName varchar(100) NOT NULL,
  info varchar(4000) NOT NULL
);

-- Table for mapping users and roles: user_boards

CREATE TABLE IF NOT EXISTS user_boards (
  user_id int NOT NULL,
  board_id int NOT NULL,

  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (board_id) REFERENCES boards(id),

  UNIQUE (user_id, board_id)
);

CREATE SEQUENCE users_id_seq MINVALUE 3;

ALTER TABLE users ALTER id SET DEFAULT nextval('users_id_seq');

ALTER SEQUENCE users_id_seq OWNED BY users.id;

CREATE SEQUENCE roles_id_seq MINVALUE 3;

ALTER TABLE roles ALTER id SET DEFAULT nextval('users_id_seq');

ALTER SEQUENCE roles_id_seq OWNED BY roles.id;

ALTER TABLE users ADD CONSTRAINT unique_username UNIQUE (username);
ALTER TABLE roles ADD CONSTRAINT unique_rolename UNIQUE (rolename);