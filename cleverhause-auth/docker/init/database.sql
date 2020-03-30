-- I had to write script .bat: invoke plsql using postgres creds after that switch on to my user and call other db scripts
CREATE ROLE clever_admin WITH LOGIN
  ENCRYPTED PASSWORD 'WindowsVista123'
  NOSUPERUSER INHERIT CREATEDB CREATEROLE NOREPLICATION;

CREATE DATABASE cleverhause_db WITH OWNER clever_admin ENCODING = 'UTF8' CONNECTION LIMIT = -1;
GRANT ALL ON DATABASE cleverhause_db TO clever_admin;

CREATE SCHEMA clever_schema
    AUTHORIZATION clever_admin;

GRANT ALL ON SCHEMA clever_schema TO clever_admin;

ALTER DEFAULT PRIVILEGES IN SCHEMA clever_schema
GRANT ALL ON TABLES TO clever_admin WITH GRANT OPTION;

ALTER DEFAULT PRIVILEGES IN SCHEMA clever_schema
GRANT SELECT, USAGE ON SEQUENCES TO clever_admin WITH GRANT OPTION;

ALTER DEFAULT PRIVILEGES IN SCHEMA clever_schema
GRANT EXECUTE ON FUNCTIONS TO clever_admin WITH GRANT OPTION;

ALTER DEFAULT PRIVILEGES IN SCHEMA clever_schema
GRANT USAGE ON TYPES TO clever_admin WITH GRANT OPTION;

-- Table: profile
-- after that I should switch on to clever_admin user. Each table will be owned by the user issuing the command

CREATE TABLE IF NOT EXISTS clever_schema.profiles (
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
ALTER TABLE clever_schema.profiles
    OWNER to clever_admin;

-- Table: users

CREATE TABLE IF NOT EXISTS clever_schema.users (
  id int NOT NULL UNIQUE PRIMARY KEY,
  username varchar(255) NOT NULL,
  password varchar(255) NOT NULL,
  CONSTRAINT voidUserName CHECK (username <> '')
);

ALTER TABLE clever_schema.users
    OWNER to clever_admin;

-- Table: roles

CREATE TABLE IF NOT EXISTS clever_schema.roles (
  id int NOT NULL UNIQUE PRIMARY KEY,
  rolename varchar(100) NOT NULL
);

ALTER TABLE clever_schema.roles
    OWNER to clever_admin;

-- Table for mapping users and roles: user_roles

CREATE TABLE IF NOT EXISTS clever_schema.user_roles (
  user_id int NOT NULL,
  role_id int NOT NULL,

  FOREIGN KEY (user_id) REFERENCES clever_schema.users(id),
  FOREIGN KEY (role_id) REFERENCES clever_schema.roles(id),

  UNIQUE (user_id, role_id)
);

ALTER TABLE clever_schema.user_roles
    OWNER to clever_admin;

-- Table: board

CREATE TABLE IF NOT EXISTS clever_schema.board (
  id int NOT NULL UNIQUE PRIMARY KEY,
  boardUID int NOT NULL UNIQUE,
  boardname varchar(100),
  user_id int NOT NULL,

  FOREIGN KEY (user_id) REFERENCES clever_schema.users(id)
);
ALTER TABLE clever_schema.board
    OWNER to clever_admin;


-- Table: boardStructure

CREATE TABLE IF NOT EXISTS clever_schema.boardStructure (
  id int NOT NULL UNIQUE PRIMARY KEY,
  structure varchar(4000),
  board_id int NOT NULL,

  FOREIGN KEY (board_id) REFERENCES clever_schema.board(id) ON DELETE CASCADE
);
ALTER TABLE clever_schema.boardStructure
    OWNER to clever_admin;

-- Table: boardControlData

CREATE TABLE IF NOT EXISTS clever_schema.boardControlData (
  id int NOT NULL UNIQUE PRIMARY KEY,
  data varchar(4000),
  created timestamp,
  board_id int NOT NULL,

  FOREIGN KEY (board_id) REFERENCES clever_schema.board(id) ON DELETE CASCADE
);
ALTER TABLE clever_schema.boardControlData
    OWNER to clever_admin;

-- Table: boardSavedData

CREATE TABLE IF NOT EXISTS clever_schema.boardSavedData (
  id int NOT NULL UNIQUE PRIMARY KEY,
  data varchar(4000),
  created timestamp,
  board_id int NOT NULL,

  FOREIGN KEY (board_id) REFERENCES clever_schema.board(id) ON DELETE CASCADE
);
ALTER TABLE clever_schema.boardSavedData
    OWNER to clever_admin;

-- Table for mapping users and roles: user_board

CREATE TABLE IF NOT EXISTS clever_schema.user_board (
  user_id int NOT NULL,
  board_id int NOT NULL,

  FOREIGN KEY (user_id) REFERENCES clever_schema.users(id),
  FOREIGN KEY (board_id) REFERENCES clever_schema.board(id),

  UNIQUE (user_id, board_id)
);
ALTER TABLE clever_schema.user_board
    OWNER to clever_admin;

-- Table for new User's board UIDs: user_new_board

CREATE TABLE IF NOT EXISTS clever_schema.user_new_board (
  id int NOT NULL UNIQUE PRIMARY KEY,
  user_id int NOT NULL UNIQUE,
  boardUID int NOT NULL UNIQUE,
  boardname varchar(100) NOT NULL,

  FOREIGN KEY (user_id) REFERENCES clever_schema.users(id) ON DELETE CASCADE
);
ALTER TABLE clever_schema.user_new_board
    OWNER to clever_admin;

CREATE SEQUENCE clever_schema.profiles_id_seq MINVALUE 1;
ALTER SEQUENCE clever_schema.profiles_id_seq OWNER TO clever_admin;
ALTER SEQUENCE clever_schema.profiles_id_seq OWNED BY clever_schema.profiles.id;
ALTER TABLE clever_schema.profiles ALTER id SET DEFAULT nextval('clever_schema.profiles_id_seq');

CREATE SEQUENCE clever_schema.users_id_seq MINVALUE 3;
ALTER SEQUENCE clever_schema.users_id_seq OWNER TO clever_admin;
ALTER SEQUENCE clever_schema.users_id_seq OWNED BY clever_schema.users.id;
ALTER TABLE clever_schema.users ALTER id SET DEFAULT nextval('clever_schema.users_id_seq');

CREATE SEQUENCE clever_schema.roles_id_seq MINVALUE 3;
ALTER SEQUENCE clever_schema.roles_id_seq OWNER TO clever_admin;
ALTER SEQUENCE clever_schema.roles_id_seq OWNED BY clever_schema.roles.id;
ALTER TABLE clever_schema.roles ALTER id SET DEFAULT nextval('clever_schema.roles_id_seq');

CREATE SEQUENCE clever_schema.board_id_seq MINVALUE 1;
ALTER SEQUENCE clever_schema.board_id_seq OWNER TO clever_admin;
ALTER SEQUENCE clever_schema.board_id_seq OWNED BY clever_schema.board.id;
ALTER TABLE clever_schema.board ALTER id SET DEFAULT nextval('clever_schema.board_id_seq');

CREATE SEQUENCE clever_schema.boardStructure_id_seq MINVALUE 1;
ALTER SEQUENCE clever_schema.boardStructure_id_seq OWNER TO clever_admin;
ALTER SEQUENCE clever_schema.boardStructure_id_seq OWNED BY clever_schema.boardStructure.id;
ALTER TABLE clever_schema.boardStructure ALTER id SET DEFAULT nextval('clever_schema.boardStructure_id_seq');

CREATE SEQUENCE clever_schema.boardControlData_id_seq MINVALUE 1;
ALTER SEQUENCE clever_schema.boardControlData_id_seq OWNER TO clever_admin;
ALTER SEQUENCE clever_schema.boardControlData_id_seq OWNED BY clever_schema.boardControlData.id;
ALTER TABLE clever_schema.boardControlData ALTER id SET DEFAULT nextval('clever_schema.boardControlData_id_seq');

CREATE SEQUENCE clever_schema.boardSavedData_id_seq MINVALUE 1;
ALTER SEQUENCE clever_schema.boardSavedData_id_seq OWNER TO clever_admin;
ALTER SEQUENCE clever_schema.boardSavedData_id_seq OWNED BY clever_schema.boardSavedData.id;
ALTER TABLE clever_schema.boardSavedData ALTER id SET DEFAULT nextval('clever_schema.boardSavedData_id_seq');

CREATE SEQUENCE clever_schema.user_new_board_id_seq MINVALUE 1;
ALTER SEQUENCE clever_schema.user_new_board_id_seq OWNER TO clever_admin;
ALTER SEQUENCE clever_schema.user_new_board_id_seq OWNED BY clever_schema.user_new_board.id;
ALTER TABLE clever_schema.user_new_board ALTER id SET DEFAULT nextval('clever_schema.user_new_board_id_seq');

--TODO for all sequence there is the same problem - owner. They need have the same owner as table for which they used

ALTER TABLE clever_schema.users ADD CONSTRAINT unique_username UNIQUE (username);
ALTER TABLE clever_schema.roles ADD CONSTRAINT unique_rolename UNIQUE (rolename);