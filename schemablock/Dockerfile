FROM openjdk:11

ENV PORT 3000
ENV SCHEMA_URL "file:///tmp/schema.jq"

EXPOSE 3000

RUN mkdir -p /mnt/schema

COPY /target/SHADED.jar /app/app.jar
WORKDIR /app

CMD ["java", "-jar", "app.jar"]