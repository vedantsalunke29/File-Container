FROM openjdk:23

LABEL image.name="config-server"

ADD target/config-server-0.0.1-SNAPSHOT.jar config-server-0.0.1-SNAPSHOT.jar

EXPOSE 8889

ENTRYPOINT [ "java","-jar","config-server-0.0.1-SNAPSHOT.jar" ]