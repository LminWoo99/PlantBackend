FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/plant-eureka-1.0.jar plant-eureka.jar
ENTRYPOINT ["java", "-jar", "plant-eureka.jar"]
