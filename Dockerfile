
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/jaysan-web-1.0.0.jar jaysan-web.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "jaysan-web.jar"]
