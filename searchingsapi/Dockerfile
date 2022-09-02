FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY target/searchingsApi-1.0.jar SearchingsApi.jar
ENTRYPOINT ["java", "-jar", "SearchingsApi.jar"]