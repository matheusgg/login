FROM openjdk:8-jre-alpine
ADD *jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar",  "/app.jar"]