FROM openjdk:23

LABEL image.name="image-storage-service"

ADD target/image-storage-service-0.0.1-SNAPSHOT.jar image-storage-service-0.0.1-SNAPSHOT.jar

EXPOSE 8091

ENTRYPOINT ["java", "-jar", "image-storage-service-0.0.1-SNAPSHOT.jar"]


