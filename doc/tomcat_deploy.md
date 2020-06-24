1. Commands for tomcat9 state changing:
 $ sudo systemctl status tomcat
 $ sudo systemctl start tomcat
 $ sudo systemctl stop tomcat
2. Tomcat9 path
 $ /opt/tomcat/late

3. If there are no deviceErrors enable the Tomcat service to be automatically started at boot time:
 $ sudo systemctl enable tomcat

4. To run Tomcat as a service we will create a new unit file. Open your text editor and create a file named tomcat.service in the /etc/systemd/system/:

   `sudo nano /etc/systemd/system/tomcat.service`

   Paste the following configuration:

   --- inside file /etc/systemd/system/tomcat.service
   `[Unit]
   Description=Tomcat 9 servlet container
   After=network.target

   [Service]
   Type=forking

   User=tomcat
   Group=tomcat

   Environment="JAVA_HOME=/usr/lib/jvm/default-java"
   Environment="JAVA_OPTS=-Djava.security.egd=file:///dev/urandom -Djava.awt.headless=true"

   Environment="CATALINA_BASE=/opt/tomcat/late"
   Environment="CATALINA_HOME=/opt/tomcat/late"
   Environment="CATALINA_PID=/opt/tomcat/late/temp/tomcat.pid"
   Environment="CATALINA_OPTS=-Xms512M -Xmx1024M -server -XX:+UseParallelGC"

   ExecStart=/opt/tomcat/late/bin/startup.sh
   ExecStop=/opt/tomcat/late/bin/shutdown.sh

   [Install]
   WantedBy=multi-user.target`

   Save and close the file and notify systemd that we created a new unit file:

   `sudo systemctl daemon-reload`

5. Tomcat user password - tomcat
6. Using logback correctly means I should use paths for logs where tomcat user has permissions.
Env vars are not working inside logback.xml.