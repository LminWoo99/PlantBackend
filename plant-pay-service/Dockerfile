FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/plant-pay-service-1.0.jar plant-pay-service.jar
ENTRYPOINT ["java", "-jar", "plant-pay-service.jar"]
