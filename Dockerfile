FROM amazoncorretto:17.0.7-alpine

WORKDIR /app
COPY target/social-media-api-*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]