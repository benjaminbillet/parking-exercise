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
