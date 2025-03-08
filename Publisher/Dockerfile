# Use official Java runtime as a base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file (Ensure you have built the project first)
COPY target/Publisher-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 8070

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
