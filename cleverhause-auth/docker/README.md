# Description
This Dockerfile will help you to test postgresql connection if you are working with local profile


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
jdbc:postgresql://localhost:5432/cleverhause_db
with 
username=clever_admin
pass=WindowsVista123

###Remember
init sqls will be processed in linguistic order (init1, init2...)

###Backup
docker exec -t -u test postgress-service pg_dumpall -c > dump_`date +%d-%m-%Y"_"%H_%M_%S`.sql

###Adminer
Postgresql
server - service_name:<inner_port> (postgresql:5432)
username - postgres
password - POSTGRES_PASSWORD

### Port forwarding in mac
$ sudo pfctl -ef port_forwards
$ sudo nano /etc/pf.conf
$ sudo pfctl -vnf port_forwards
$ dscacheutil -flushcache - need only to clean cache of dns names after making changes in /etc/hosts
$ sudo killall -HUP mDNSResponder
$ sudo nano /etc/hosts
$ cd /etc/pf.anchors
$ sudo mkdir /etc/pf.anchors/port_forwards
$ mkdir /etc/pf.anchors/port_forwards
$ sudo pfctl show
$ sudo iptables -t nat -A OUTPUT -p tcp --dport 80 -d cleverhause.ru -j DNAT --to-destination 127.0.0.1:8080
$ sudo killall -HUP mDNSResponder
$ cd /private/etc/hosts
$ 
