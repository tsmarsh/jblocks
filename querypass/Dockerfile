FROM openjdk:11

ENV PORT 3000
ENV PATH_PREFIX ""
ENV QUERY_URL "file:///tmp/schema.jq"
ENV NEXT "http://localhost:4000"

EXPOSE 3000

RUN mkdir -p /mnt/schema

COPY /target/SHADED.jar /app/app.jar
WORKDIR /app

CMD ["java", "-jar", "app.jar"]