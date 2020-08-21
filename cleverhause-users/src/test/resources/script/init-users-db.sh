#!/bin/bash
set -e
echo 'start using psql inside docker container'
echo "user = $POSTGRES_USER"
echo "port = $POSTGRES_PORT"
echo "database = $POSTGRES_DB"
echo "url = $POSTGRES_URL"
psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" <<-EOSQL
\i /var/lib/postgresql/init/initUsersDb.sql;
EOSQL