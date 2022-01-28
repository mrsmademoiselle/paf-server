FROM openjdk:17-jdk-alpine
EXPOSE 9090
EXPOSE 8888
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
