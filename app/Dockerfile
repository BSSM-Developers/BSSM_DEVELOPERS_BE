# Runtime-only image. Build the jar locally before running docker build.
FROM --platform=linux/amd64 eclipse-temurin:21-jre

WORKDIR /app

COPY app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
