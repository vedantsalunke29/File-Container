# Use a Java base image for the build stage
FROM openjdk:23-jdk-slim as builder

# Set the working directory inside the container
WORKDIR /app

# Copy the local Maven project into the container
COPY . /app

# Set the environment variable for the build command
ENV MAVEN_OPTS="-Xmx512m -Xms256m"

# Install Maven (if needed) and build the project
RUN apt-get update && apt-get install -y maven
RUN mvn clean package -DskipTests

# Use a smaller base image to run the application
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file built by Maven from the builder stage
COPY --from=builder /app/target/auth-service-0.0.1-SNAPSHOT.jar /app/auth-service.jar

# Expose the port the app will run on
EXPOSE 8093

# Set the entry point for the application
ENTRYPOINT ["java", "-jar", "/app/auth-service.jar"]
