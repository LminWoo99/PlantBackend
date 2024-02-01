FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/plant-gateway-service-1.0.jar plant-gateway-service.jar
ENTRYPOINT ["java", "-jar", "plant-gateway-service.jar"]
