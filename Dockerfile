FROM eclipse-temurin:17-jdk-focal
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080 5432
ENTRYPOINT ["java", "-jar", "/app.jar"]