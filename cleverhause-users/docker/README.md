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