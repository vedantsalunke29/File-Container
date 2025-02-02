# Use an OpenJDK base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the local Maven project into the container
COPY . /app

# Set the environment variable for the build command
ENV MAVEN_OPTS="-Xmx512m -Xms256m"

# Install Maven and build the project
RUN apt-get update && apt-get install -y maven
RUN mvn clean package -DskipTests

# Copy the JAR file built by Maven
COPY --from=builder /app/target/discovery-server-0.0.1-SNAPSHOT.jar /app/discovery-server.jar

# Expose the port the Eureka Server will run on
EXPOSE 8761

# Set the entry point to run the Eureka Server JAR
ENTRYPOINT ["java", "-jar", "/app/discovery-server.jar"]
