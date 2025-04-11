# Use Alpine Linux with JDK 21
FROM eclipse-temurin:21-jdk-alpine

VOLUME /bankapp

COPY ./target/bankapp-0.0.1-SNAPSHOT.jar /bankapp.jar

ENV JAVA_OPTS=""

ENV TZ=""

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /bankapp.jar" ]