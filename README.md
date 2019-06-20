# Parking Toll Exercise
Note: this project is based on a bigger demo project created for educational purposes: https://github.com/benjaminbillet/spring-architecture-example

Table of Content (generated with [markdown-toc](http://ecotrust-canada.github.io/markdown-toc))
- [How to run in dev mode?](#how-to-run-in-dev-mode-)
- [How to run the unit tests?](#how-to-run-the-unit-tests-)
- [How to setup code analysis?](#how-to-setup-code-analysis-)
- [How to build and deploy for production?](#how-to-build-and-deploy-for-production-)
- [The API in a nutshell](#the-api-in-a-nutshell)
- [Architecture Overview](#architecture-overview)

## How to run in dev mode?
1. make sure you have [mariadb](https://mariadb.com) installed.

2. create your `parkingtoll` database and populate it using the `sql/populate.sql` file:
```bash
mysql -u <your user> -p < sql/populate.sql
```

3. run the application (in `dev` mode, we have `ddl-auto=update`)
```bash
SPRING_PROFILES_ACTIVE=dev ./gradlew bootRun
```

## How to run the unit tests?
Run the tests:
```bash
./gradlew test
```
Jacoco reports are generated into `<project folder>/build/reports/tests/test/index.html`.

## How to setup code analysis?
This project use the [SonarQube](https://www.sonarqube.org) inspector. You can setup a local instance in a few minutes: https://docs.sonarqube.org/latest/setup/get-started-2-minutes. 

Once the SonarQube local server is running (`http://localhost:9000`, configurable in `sonar.gradle`), use `./gradlew sonarqube` to trigger analyses. 

## How to build and deploy for production?
1. First, build a Spring Boot fat jar:
```
./gradlew bootJar
```

2. Create a docker image from the provided `Dockerfile`:
```
sudo docker build -t benjaminbillet/parkingtoll --build-arg env="production" --build-arg dbUser="<your password>" --build-arg dbPassword="<your password>" .
```
Note: instead of using docker, you can use directly the generated fat jar. However you will have to provide all environment variables yourself (see the `Dockerfile` for more information).

3. The first time, create your `parkingtoll` database and populate it using the `sql/populate.sql` file:
```bash
mysql -u <your user> -p < sql/populate.sql
```

4. Run the image (`--net=host` is only required if your database is running on localhost):
```
sudo docker run --net=host -p 8080:8080 benjaminbillet/parkingtoll
```

## The API in a nutshell
Checkin service:
```
curl -X POST http://localhost:8080/api/parking/{parkingId}/spots/byType/{spotTypeId} -v
```

Checkout service:
```
curl -X DELETE http://localhost:8080/api/parking/{parkingId}/cars/{carId} -v
```

Check also `src/main/java/resources/swagger.yaml` for the full specification.

Other useful endpoints (restricted to local calls only):
- `/actuator/prometheus`: Prometheus-formatted metrics. You can setup a [local Prometheus](https://prometheus.io/docs/prometheus/latest/getting_started) in a few minutes.
- `/actuator/health`: A health probe, that can be used to create Kubernetes liveness or readiness probes.

## Architecture Overview
Layers:
- `repository`, `domain`, `dto` packages respectively contains the data access layer, the domain entities and the data transfer objects.
- `service` package contains the two services of the application: spot allocation and billing. Both are well-separated, ensuring that the billing service could be externalized as a separated micro-service when more complex billing use-case will be introduced (e.g., a rule engine with various pricing policies).
  - Parking spot allocation service: provide `checkin` (find a free spot of a given type and allocate it for the car) and `checkout` (free a previously allocated spot and bill the customer) primitives.
  - Billing service: provide `computeBill` primitive, that relies on a factory for building pricing policy implementations.
- `api` package contains the REST controllers that wrap the parking spot allocation service.
