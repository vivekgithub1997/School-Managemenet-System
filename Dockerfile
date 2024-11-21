# Start with a base image with JDK
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the application JAR file to the container
COPY target/School-Management-Software-0.0.1-SNAPSHOT.jar School-Management-Software.jar

# Expose the port your application runs on (e.g., 9090)
EXPOSE 9090

# Run the JAR file
ENTRYPOINT ["java", "-jar", "School-Management-Software.jar"]
