# Cleverhause.ru

  The main project aim is developing of soft-electronic system for using in «clever» house. This system is remotely controlled disturbed automatic system, which is controlled through internet.

## Description

  The system consist of main block named Board (based on Arduino Mega) and up to 8 standalone control-measuring modules named Devices (based on Arduino ProMini). Board connects to Wi-Fi router and interacts with server. Board has LCD display and clipboard for passing connection info - login, passwords and so on and so forth. Server is implemented on Tomcat 7.0 and deployed on AWS T2.micro for testing. URL: www.cleverhause.ru/cleverhause/home (cleverhOuse was busy). All previously said is presented at picture below.
   
![alt text](https://user-images.githubusercontent.com/28635427/46251011-a89fa100-c458-11e8-8560-26471eadeb6c.jpg "Cleverhause structure")
Picture 1. Cleverhause structure
  Main flow description. Customer registers all Devices in to the Board following by special procedure. After that he should register on site and get number for his new Board. This number is saved to the Board. Server has special Rest API. Using it Board can register yourself on server and send to him data from Devices in a work state. After successful registration customer can manage Devices using tab «Boards» on site. There he can change control and check measured data of every Device. After submit changes are saved into Database. Board periodically requests server and server sends back changed values of control signals from DB. Board transmit all appropriated data to Devices through radio channel.



### Prerequisites

Technologies: Java 7, Spring Core, Spring MVC, Spring Security, Hibernate, JSP, JSTL, Bootstrap, maven
VCS: Git
DB: PostgreSQL 9.6
Tools: IntellijIdea

## Deployment

Add additional notes about how to deploy this on a live system

## Authors

* **Aleksandr Ivanov** - *Initial work* - [IskanderIV](https://github.com/IskanderIV)
