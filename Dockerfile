# Base image for Java 17
FROM bellsoft/liberica-openjdk-alpine:17 as builder

# Set working directory
WORKDIR /app

COPY .env /app/.env

# Copy build files and Gradle wrapper
COPY . .

# Build the application
RUN ./gradlew clean build -x test

# Production image
FROM bellsoft/liberica-openjdk-alpine:17

# Set working directory
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Copy the .env file from the builder stage
COPY --from=builder /app/.env .env

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]