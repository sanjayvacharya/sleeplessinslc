FROM openjdk:8-jdk-alpine

ENV PORT 8080

ARG JAR_FILE

EXPOSE 8080

ADD target/${JAR_FILE} app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]