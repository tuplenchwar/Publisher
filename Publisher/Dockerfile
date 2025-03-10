# Use official Java runtime as a base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/

# Copy the built JAR file into the container
COPY  out/artifacts/Publisher-1.0-SNAPSHOT.jar  /app/Publisher.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "Publisher.jar"]
