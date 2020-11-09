#!/bin/bash
set -e
#sudo –i –u $POSTGRES_USER psql
psql -U "$POSTGRES_USER" <<-EOSQL
CREATE USER clever_admin WITH ENCRYPTED PASSWORD 'WindowsVista123' NOSUPERUSER INHERIT CREATEDB CREATEROLE NOREPLICATION;
CREATE DATABASE cleverhause_users_db WITH OWNER clever_admin ENCODING = 'UTF8' CONNECTION LIMIT = -1;
GRANT ALL ON DATABASE cleverhause_users_db TO clever_admin;
\c cleverhause_users_db clever_admin;
\i /var/lib/postgresql/init/initUsersDb.sql;
EOSQL