FROM openjdk:23

# Set the working directory
WORKDIR /app

# Copy the JAR file into the container
ADD target/image-storage-service-0.0.1-SNAPSHOT.jar /app/image-storage-service-0.0.1-SNAPSHOT.jar

# Expose the correct port
EXPOSE 8091

# Run the application
ENTRYPOINT ["java", "-jar", "image-storage-service-0.0.1-SNAPSHOT.jar"]


