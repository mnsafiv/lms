FROM openjdk:17-alpine
WORKDIR /app
COPY build/libs/lms-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
ENTRYPOINT ["java","-jar","lms-0.0.1-SNAPSHOT.jar"]