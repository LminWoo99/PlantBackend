FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/plant-config-server-1.0.jar plant-config-server.jar
ENTRYPOINT ["java", "-jar", "plant-config-server.jar"]
