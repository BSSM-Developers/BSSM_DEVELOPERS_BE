# Build stage
FROM --platform=linux/amd64 gradle:8.5-jdk21 AS builder

WORKDIR /build

# Copy gradle files
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

# Copy all modules (including common)
COPY common ./common
COPY proxy-service ./proxy-service

# Build the application (common will be built as dependency)
RUN ./gradlew :proxy-service:bootJar --no-daemon

# Runtime stage
FROM --platform=linux/amd64 eclipse-temurin:21-jre

WORKDIR /app

# Copy the built jar from builder
COPY --from=builder /build/proxy-service/build/libs/*.jar proxy-service.jar

# Expose port
EXPOSE 8081

# Run the application
ENTRYPOINT ["java", "-jar", "proxy-service.jar"]
