FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/*.jar /app/assessment-users-service.jar

EXPOSE 8088

ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "assessment-users-service.jar"]