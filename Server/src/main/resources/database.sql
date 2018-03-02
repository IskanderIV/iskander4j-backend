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