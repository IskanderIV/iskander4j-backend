# Description
This Dockerfile will help you to test mongo connection if you are working with local profile


### Launching
 In terminal cd to '/path/to/current/project/docker/mongo' folder:
$ docker-compose up --build mongodb_devices

after that you can connect to your local mongo through
uri: mongodb://admin:admin@localhost:27017/cleverhause_devices_db?authMechanism=SCRAM-SHA-1