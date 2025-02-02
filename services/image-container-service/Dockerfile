FROM openjdk:23


LABEL image.name="image-container-service"


ADD target/image-container-service-0.0.1-SNAPSHOT.jar image-container-service-0.0.1-SNAPSHOT.jar

# Expose the correct port
EXPOSE 8090

# Run the application
ENTRYPOINT ["java", "-jar", "image-container-service-0.0.1-SNAPSHOT.jar"]