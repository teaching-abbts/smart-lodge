FROM gradle:8.8.0-jdk21-alpine AS kotlin_build
ARG ROOT_FOLDER="."
RUN apk add --no-cache nodejs npm
WORKDIR "/source"
COPY "$ROOT_FOLDER" "."
RUN gradle \
    -Ddevelopment=false \
    -Dorg.gradle.vfs.watch=false \
    -Dorg.gradle.parallel=false \
    install-vue build-vue buildFatJar

FROM alpine:3.20 AS runtime
RUN apk add --no-cache openjdk21-jre
WORKDIR "/app"
COPY --from=kotlin_build "/source/src/main/vue-project/dist" "src/main/vue-project/dist"
COPY --from=kotlin_build "/source/build/libs/smart-lodge-all.jar" "."
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/smart-lodge-all.jar"]
