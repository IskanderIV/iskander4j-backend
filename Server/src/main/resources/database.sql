CREATE ROLE clever_admin LOGIN
  ENCRYPTED PASSWORD 'md593d1079655679037dd4ec5caf1b8e91a'
  NOSUPERUSER INHERIT CREATEDB NOCREATEROLE NOREPLICATION;

-- Table: profile

CREATE TABLE IF NOT EXISTS profiles (
  id int NOT NULL UNIQUE PRIMARY KEY,
  username varchar(255) NOT NULL,
  sessionId varchar(255),
  isExpired varchar(255),
  isLogged varchar(255),
  address varchar(255),
  phone varchar(255),
  created timestamp,
  loginTime timestamp,

  CONSTRAINT voidUserName CHECK (username <> '')
);

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

-- Table: board

CREATE TABLE IF NOT EXISTS board (
  id int NOT NULL UNIQUE PRIMARY KEY,
  boardUID int NOT NULL UNIQUE,
  boardname varchar(100),
  user_id int NOT NULL,

  FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Table: boardStructure

CREATE TABLE IF NOT EXISTS boardStructure (
  id int NOT NULL UNIQUE PRIMARY KEY,
  structure varchar(4000),
  board_id int NOT NULL,

  FOREIGN KEY (board_id) REFERENCES board(id)
);

-- Table: boardControlData

CREATE TABLE IF NOT EXISTS boardControlData (
  id int NOT NULL UNIQUE PRIMARY KEY,
  data varchar(4000),
  created timestamp,
  board_id int NOT NULL,

  FOREIGN KEY (board_id) REFERENCES board(id)
);


-- Table: boardSavedData

CREATE TABLE IF NOT EXISTS boardSavedData (
  id int NOT NULL UNIQUE PRIMARY KEY,
  data varchar(4000),
  created timestamp,
  board_id int NOT NULL,

  FOREIGN KEY (board_id) REFERENCES board(id)
);

-- Table for mapping users and roles: user_board

CREATE TABLE IF NOT EXISTS user_board (
  user_id int NOT NULL,
  board_id int NOT NULL,

  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (board_id) REFERENCES board(id),

  UNIQUE (user_id, board_id)
);

CREATE SEQUENCE profiles_id_seq MINVALUE 1;
ALTER TABLE profiles ALTER id SET DEFAULT nextval('profiles_id_seq');
ALTER SEQUENCE profiles_id_seq OWNED BY profiles.id;

CREATE SEQUENCE users_id_seq MINVALUE 3;
ALTER TABLE users ALTER id SET DEFAULT nextval('users_id_seq');
ALTER SEQUENCE users_id_seq OWNED BY users.id;

CREATE SEQUENCE roles_id_seq MINVALUE 3;
ALTER TABLE roles ALTER id SET DEFAULT nextval('roles_id_seq');
ALTER SEQUENCE roles_id_seq OWNED BY roles.id;

CREATE SEQUENCE board_id_seq MINVALUE 1;
ALTER TABLE board ALTER id SET DEFAULT nextval('board_id_seq');
ALTER SEQUENCE board_id_seq OWNED BY board.id;

CREATE SEQUENCE boardStructure_id_seq MINVALUE 1;
ALTER TABLE boardStructure ALTER id SET DEFAULT nextval('boardStructure_id_seq');
ALTER SEQUENCE boardStructure_id_seq OWNED BY boardStructure.id;

CREATE SEQUENCE boardControlData_id_seq MINVALUE 1;
ALTER TABLE boardControlData ALTER id SET DEFAULT nextval('boardControlData_id_seq');
ALTER SEQUENCE boardControlData_id_seq OWNED BY boardControlData.id;

CREATE SEQUENCE boardSavedData_id_seq MINVALUE 1;
ALTER TABLE boardSavedData ALTER id SET DEFAULT nextval('boardSavedData_id_seq');
ALTER SEQUENCE boardSavedData_id_seq OWNED BY boardSavedData.id;

ALTER TABLE users ADD CONSTRAINT unique_username UNIQUE (username);
ALTER TABLE roles ADD CONSTRAINT unique_rolename UNIQUE (rolename);