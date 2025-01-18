# Use a lightweight Java 21 image as the base
FROM eclipse-temurin:21-jdk-jammy

# Set the working directory in the container
WORKDIR /app

# Copy the application JAR file into the container
COPY target/Reminder-Call-0.0.1-SNAPSHOT.jar app.jar

# Expose the port on which your Spring Boot app runs
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
