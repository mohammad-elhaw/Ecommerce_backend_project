FROM openjdk:20

WORKDIR app
EXPOSE 8080

COPY e-commerce/target/docker.jar /app/docker.jar

ENTRYPOINT ["java", "-jar", "/app/docker.jar"]