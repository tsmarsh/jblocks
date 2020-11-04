FROM gcr.io/distroless/java:11

ENV PORT 3000
ENV SCHEMA_URL "file:///tmp/schema.jq"

EXPOSE 3000

COPY /target/SHADED.jar /app/app.jar
WORKDIR /app

CMD ["app.jar"]