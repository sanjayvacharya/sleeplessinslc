FROM anapsix/alpine-java
VOLUME /tmp
ADD ${project.build.finalName}.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 9094
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
