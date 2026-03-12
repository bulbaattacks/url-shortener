FROM eclipse-temurin:21-jre-alpine
VOLUME /tmp

COPY target/url-shortener-0.0.1-SNAPSHOT.jar app.jar
COPY .env .env

ENTRYPOINT ["/bin/sh", "-c", "set -a && . /.env && set +a && exec java -jar /app.jar"]
