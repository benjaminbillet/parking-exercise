# Parking Toll Exercise


## How to run in dev mode?
1. make sure you have [mariadb](https://mariadb.com) installed.

2. create your `parkingtoll` database:
```bash
mysql -u <your user> -p -e "create database if not exists parkingtoll character set utf8mb4 collate utf8mb4_bin;"
```

3. run the application (in dev mode, we have `ddl-auto=update`)
```bash
SPRING_PROFILES_ACTIVE=dev ./gradlew bootRun
```

## Run the tests
Run the tests:
```bash
./gradlew test
```
Jacoco reports are generated into `<project folder>/build/reports/tests/test/index.html`.

# Code analysis
This project use the [SonarQube](https://www.sonarqube.org) inspector. You can setup a local instance in a few minutes: https://docs.sonarqube.org/latest/setup/get-started-2-minutes. 

Once the SonarQube local server is running (`http://localhost:9000`, configurable in `sonar.gradle`), use `./gradlew sonarqube` to trigger analyses. 
