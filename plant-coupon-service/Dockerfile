FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/plant-coupon-service-1.0.jar plant-coupon-service.jar
ENTRYPOINT ["java", "-jar", "plant-coupon-service.jar"]
