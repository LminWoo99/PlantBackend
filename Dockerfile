FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/PlantProject-1.0.jar PlantProject.jar
ENTRYPOINT ["java", "-jar", "PlantProject.jar"]
