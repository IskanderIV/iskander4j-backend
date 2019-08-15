# Cleverhause.ru

  The main project aim is developing of soft-electronic system for using in «clever» house. This system is remotely controlled disturbed automatic system, which is controlled through internet.

## Description

  The system consist of main block named Board (based on Arduino Mega) and up to 8 standalone control-measuring modules named Devices (based on Arduino ProMini). Board connects to Wi-Fi router and interacts with server. Board has LCD display and clipboard for passing connection info - login, passwords and so on and so forth. Server is implemented on Tomcat 7.0 and deployed on AWS T2.micro for testing. URL: cleverhause.ru/cleverhause/home (cleverhOuse was busy). All previously said is presented at picture 1 below.
   
![alt text](https://user-images.githubusercontent.com/28635427/46251011-a89fa100-c458-11e8-8560-26471eadeb6c.jpg "Cleverhause structure")
Picture 1. Cleverhause structure

  Main flow description. Customer registers all Devices in to the Board following by special procedure. After that he should register on site and get number for his new Board. This number is saved to the Board. Server has special Rest API. Using it Board can register yourself on server and send to him data from Devices in a work state. After successful registration customer can manage Devices using tab «Boards» on site. There he can change control and check measured data of every Device. After submit changes are saved into Database. Board periodically requests server and server sends back changed values of control signals from DB. Board transmit all appropriated data to Devices through radio channel.

Example of using cleverhause system is presented at picture 2 below.

![alt text](https://user-images.githubusercontent.com/28635427/46251025-f3b9b400-c458-11e8-841a-68ccfe8b8f97.jpg "Cleverhause example of using")
Picture 2. Cleverhause example of using

Prototypes of Board and Device are presented at Picture 3 and 4 correspondingly.

![alt text](https://user-images.githubusercontent.com/28635427/46262286-2ecaee80-c510-11e8-92f2-bff4a55c7b76.jpg "Cleverhause Board prototype")
Picture 3. Cleverhause Board prototype

![alt text](https://user-images.githubusercontent.com/28635427/46262289-4904cc80-c510-11e8-9b3b-a36026b2e790.jpg "Cleverhause Device prototype")
Picture 4. Cleverhause Device prototype

### Prerequisites

1. Technologies: Java 7, Spring Core, Spring MVC, Spring Security, Hibernate, JSP, JSTL, Bootstrap, maven
2. VCS: Git
3. DB: PostgreSQL 9.6
4. Tools: IntellijIdea

## Deployment

1. Take AWS account following procedure described on AWS site http://aws.amazon.com/.
2. Connect to your instance through ssh using PuTTY.
3. Install Oracle’s JDK. 
```$ sudo wget --no-cookies --no-check-certificate --header "Cookie: %3A%2F%2Fwww.oracle.com%2F; -securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/8u151-b12/e758a0de34e24606bca991d704f6dcbf/jdk-8u151-linux-x64.tar.gz
$ sudo tar xzf jdk-8u151-linux-x64.tar.gz
$ cd jdk1.8.0_151/
$ sudo alternatives --install /usr/bin/java java /opt/jdk1.8.0_151/bin/java 2
$ sudo alternatives --config java
# There are maybe more than one programs which provide 'java'. So you need to choose only one that corresponds to jdk1.8.0_151
# Checking if the server’s default java version is Sun JDK (usually it is OpenJDK)
$ java -version
```
4. Install Tomcat 7.
```
$ sudo yum install tomcat7
# Starting Tomcat
$ sudo service tomcat7 start
# Installing Tomcat documentation, examples & webapps administrator
$ sudo yum install tomcat7-webapps tomcat7-docs-webapp tomcat7-admin-webapps
# Checking that tomcat is running fine in port 8080:
$ sudo fuser -v -n tcp 8080
 
# You’ll get something like this if everything’s fine
                        USER    PID  ACCESS  COMMAND
 8080/tcp:              tomcat  pid  F….     java
# Stopping Tomcat (only use it if you need to stop the server)
$ sudo service tomcat7 stop
```
5. Install Postgresql. 
```
# Install the repository RPM:
$ sudo yum install https://download.postgresql.org/pub/repos/yum/9.6/redhat/rhel-6-x86_64/pgdg-ami201503-96-9.6-2.noarch.rpm
# Install the client packages:
$ sudo yum install postgresql96
$ sudo yum install postgresql96-server
# init database
$ sudo service postgresql96 initdb 
# after that directory /var/lib/pgsql96 will be created
# If you want PostgreSQL to start automatically when the OS starts, do this
$ sudo chkconfig postgresql96 on

# change config file of database
$ sudo vim /var/lib/pgsql96/data/pg_hba.conf

# Update the bottom of the file, which will read something like this, by default:

# TYPE  DATABASE        USER            ADDRESS                 METHOD
 
# "local" is for Unix domain socket connections only
local   all             all                                     ident
# IPv4 local connections:
host    all             all             127.0.0.1/32            ident - The first question many ask is, “What is the default password for the user postgres?” The answer is easy… there isn’t a default password. The default authentication mode for PostgreSQL is set to ident.
# IPv6 local connections:
host    all             all             ::1/128                 ident
 
To read this:

# TYPE  DATABASE        USER            ADDRESS                 METHOD
 
# "local" is for Unix domain socket connections only
local   all             all                                     trust
# IPv4 local connections:
host    all             my_user      0.0.0.0/0                  md5
# IPv6 local connections:
host    all             all             ::1/128                 md5

5) postgres connection configurations
$ sudo vim /var/lib/pgsql96/data/postgresql.conf
# Uncomment and edit the following lines -
#listen_addresses = 'localhost'
to
listen_addresses='*'
and
#port = 5432
to
port = 5432

6) Start PostgreSQL server
$ sudo service postgresql96 start
# start : start the database
# stop : stop the database
# restart : stop/start the database; used to read changes to core configuration files
# $ sudo service postgresql96 reload : reload pg_hba.conf file while keeping database running

7) $ sudo su - postgres
8) Login into Postgres user. Out - Ctrl+Z
$ psql -U postgres

9) Change postgres login password
ALTER USER postgres WITH PASSWORD '$password';

10) CREATE ROLE my_user WITH LOGIN
  ENCRYPTED PASSWORD '$userPassword'
  NOSUPERUSER INHERIT CREATEDB CREATEROLE NOREPLICATION;
  
11) set role;

12) CREATE DATABASE cleverhause_db WITH OWNER my_user ENCODING = 'UTF8' CONNECTION LIMIT = -1;
GRANT ALL ON DATABASE cleverhause_db TO my_user; - TODO think about template1 or template0

13) (maybe the same as in 3 b) Autostart of postgresql
$ sudo vim /etc/systemd/system/multi-user.target.wants/postgresql.service
Environment=PGROOT=/pathto/postgresql/
PIDFile=/pathto/postgresql/data/postmaster.pid

Delete postgres through 
$ sudo yum remove postgres\*
using http://it-admin.org/tag/remove-postgresql I've found:
$ ssudo yum --purge remove postgresql\*
After that do removing described below:
$ ssudo rm -r /etc/postgresql/
$ ssudo rm -r /etc/postgresql-common/
$ ssudo rm -r /etc/sysconfig/pgsql
$ ssudo rm -r /var/lib/pgsql96/
$ ssudo userdel -r postgres
$ ssudo groupdel postgres
```
6. Copy war from localhost to server
scp -i path/to/ssh_key.ppk path/to/cleverhause.war <amazonHostName>:/home/ec2-user
Or just use WinSCP

7. Deploy
```
# get root permission: 
$ sudo su root
# go to the home folder: 
$ cd /home/ec2-user
# copy war to tomcat: 
$ cp cleverhause.war /usr/share/tomcat7/webapps/
# start tomcat: 
$ sudo service tomcat7 start
```

8. Stoping tomcat (if needed)
```
$ sudo service tomcat7 stop
```

9. Remove old project from tomcat 
```
..a) for folders 
rm -r -I /usr/share/tomcat7/webapps/cleverhause/
If the -I or --interactive=once option is given, 
and there are more than three files or the -r, -R, 
or --recursive are given, then rm prompts the user 
for whether to proceed with the entire operation. 
If the response is not affirmative, the entire command is aborted.
Otherwise, if a file is unwritable, standard input is a terminal, 
and the -f or --force option is not given, or the -i or 
--interactive=always option is given, rm prompts the user 
for whether to remove the file. If the response is not 
affirmative, the file is skipped.

..b) for files
rm /usr/share/tomcat7/webapps/cleverhause.war
```
10. Setting tomcat to run in ports 80 & 443
There are many ways to achieve this. Some of them need you to install an apache server, and handle the different XML configuration files, but I think that this is a quite easy way, and you don’t need to install anything else, you just need to learn a little bit on how to use iptables so you can redirect the requests from port 80 to 8080 and from port 443 to 8443.
To do so, you can type in the following commands in your console:

```
# Checking the current iptables content on the prerouting table
$ sudo /sbin/iptables -L -n -t nat
# Adding the rules:
$ sudo /sbin/iptables -t nat -I PREROUTING -p tcp —-dport 80 -j REDIRECT --to-port 8080
$ sudo /sbin/iptables -t nat -I PREROUTING -p tcp --dport 443 -j REDIRECT --to-port 8443
# Checking that the rules were added succesfully
$ sudo /sbin/iptables -L -n -t nat
# Saving the rules
$ sudo /sbin/service iptables save
# Restarting ipTables:
$ sudo /etc/init.d/iptables restart
# Checking that the rules were saved succesfully
$ sudo /sbin/iptables -L -n -t nat
```

## Authors

* **Aleksandr Ivanov** - *All project work* - [IskanderIV](https://github.com/IskanderIV)
* **Ricardo Alvarado** - *Readme.md deployment part* - [Ricardo Alvarado](https://medium.com/@rijoalvi/setting-up-an-amazon-ec2-aws-server-with-tomcat7-running-on-ports-80-443-mysql-oracle-jdk-1-68bcc42bdb94)
