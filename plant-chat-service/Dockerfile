FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/plant-chat-service-1.0.jar plant-chat-service.jar
ENTRYPOINT ["java", "-jar", "plant-chat-service.jar"]
