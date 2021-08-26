# Description
This Dockerfile will help you to test postgresql-users connection if you are working with local profile


### Launching
 In terminal cd to 'docker' folder:
$ docker-compose up --build

For reinitializing postgres you need to do:
1) delete the whole files from data folder
2) prune all containers
$ docker container prune -f
this removed only unusable containers. If some of them was started before
prune was made then they were not pruned
3) prune all volumes
$ docker volume prune -f

after that you can connect to your local postgresql through
[jdbc:postgresql://localhost:5433/cleverhause_users_db]
with 
username=clever_admin
pass=WindowsVista123

###Remember
init sqls will be processed in linguistic order (init1, init2...)

###Backup
docker exec -t -u test postgress-service pg_dumpall -c > dump_`date +%d-%m-%Y"_"%H_%M_%S`.sql

###Adminer
I want to take adminer from the postgresql-users docker container
Postgresql
server - postgresql-users:5432 // because adminer sees 5432
username - clever_admin
password - WindowsVista123
db - cleverhause_users_db


$: docker exec -it docker_postgresql-users_1 bash
$: su postgres
$: psql -U postgres
$: postgres=#
$: postgres=> select current_user;
    current_user
   --------------
    clever_admin
   (1 row)
   
$: postgres=> show searchpath;
ERROR:  unrecognized configuration parameter "searchpath"
postgres=> show search_path;
   search_path
-----------------
 "$user", public
(1 row)

$: postgres=> select current_database();
 current_database
------------------
 postgres
(1 row)

docker logs -f docker_postgresql-users_1

Using database tool by idea do not forget to check 
'Manage Shown schemas' in menu. 
Needed schema could be simply switched off

`\dt *.*` - shows all schemas and tables for current user
`SELECT * FROM information_schema.tables WHERE table_schema = 'public'`

#CREATE USER clever_admin WITH ENCRYPTED PASSWORD 'WindowsVista123' NOSUPERUSER INHERIT CREATEDB CREATEROLE NOREPLICATION;
#CREATE DATABASE cleverhause_users_db WITH OWNER clever_admin ENCODING = 'UTF8' CONNECTION LIMIT = -1;
#GRANT ALL ON DATABASE cleverhause_users_db TO clever_admin;
#\c cleverhause_users_db clever_admin;