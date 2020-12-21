Leaderboard Project - GoodJobGames
===================================

### Technologies/Tools Used

1. Spring Boot Framework with maven dependencies
2. Redis Hash Set and Redis Ordered Set
3. Google Cloud MySQL Database 
4. Google Cloud Compute Engine for deployment


### Prerequistes for deployment in a linux machine

```bash
sudo apt-get update
sudo apt-get upgrade
```
1. Install Java
```bash
sudo apt-get install openjdk-8-jdk
```
2. Install Git
```bash
sudo apt-get install git
```
3. Install Maven
```bash
sudo apt install maven
```
4. Install Redis Server / Start & Stop Redis Server
```bash
sudo apt-get install redis-server
sudo service redis-server start
sudo service redis-server stop
```
5. Install Mysql
```bash
sudo apt install mysql-server
```

### Run the application

Go to directory of the project and run
```bash
mvn spring-boot:run
```

### Swagger UI

Go to url:http://34.72.148.39:8090/swagger-ui.html#
