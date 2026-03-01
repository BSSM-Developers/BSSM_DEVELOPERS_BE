FROM --platform=linux/amd64 eclipse-temurin:21-jre

WORKDIR /app

COPY proxy-service/build/libs/*.jar proxy-service.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "proxy-service.jar"]
