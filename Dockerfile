FROM gradle:8.8.0-jdk21-alpine AS build
WORKDIR /source
COPY ./smart-lodge .
RUN gradle build
CMD gradle run
