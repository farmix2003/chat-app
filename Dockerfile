# Use a base image with Java 17
FROM openjdk:21-jdk-slim
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy the Maven/Gradle build output (JAR file)
COPY target/chatApp-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port (default 8080)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]