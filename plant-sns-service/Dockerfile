FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/plant-sns-service-1.0.jar plant-sns-service.jar
ENTRYPOINT ["java", "-jar", "plant-sns-service.jar"]
