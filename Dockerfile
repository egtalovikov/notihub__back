FROM openjdk:17-jdk-alpine

ENV POSTGRES_USER=rhogoron
ENV POSTGRES_PASSWORD=rhogoron
ENV POSTGRES_DB=postgres

WORKDIR /app

COPY server.jar /app/server.jar

EXPOSE 8080

RUN apk add --no-cache postgresql-client

CMD sh -c "java -jar server.jar"
