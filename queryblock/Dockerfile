FROM gcr.io/distroless/java:11

ENV PORT 3000
ENV QUERY_URL "file:///tmp/query.jq"

EXPOSE 3000

COPY /target/golly-*-standalone.jar /app/golly.jar
WORKDIR /app

CMD ["golly.jar"]