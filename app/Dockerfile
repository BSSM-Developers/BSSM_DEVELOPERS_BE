# Build stage
FROM --platform=linux/amd64 gradle:8.5-jdk21 AS builder

WORKDIR /build

# Copy gradle files
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

# Copy all modules (including common)
COPY common ./common
COPY app ./app

# Build the application (common will be built as dependency)
RUN ./gradlew :app:bootJar --no-daemon

# Runtime stage
FROM --platform=linux/amd64 eclipse-temurin:21-jre

WORKDIR /app

# Copy the built jar from builder
COPY --from=builder /build/app/build/libs/*.jar app.jar

# Expose port (default Spring Boot port)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
