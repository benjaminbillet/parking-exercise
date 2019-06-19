FROM openjdk:10-jre-slim
LABEL MAINTAINER="Benjamin Billet - contact@benjaminbillet.fr"

COPY build/libs/parking-exercise-0.0.1-SNAPSHOT.jar /
WORKDIR /

EXPOSE 8080

ARG env
ARG dbPassword
ARG dbUser

ENV SPRING_PROFILES_ACTIVE=$env
ENV DB_USER=$dbUser
ENV DB_PASSWORD=$dbPassword

CMD ["java", "-jar", "./parking-exercise-0.0.1-SNAPSHOT.jar"]
