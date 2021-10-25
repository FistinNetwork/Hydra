FROM gradle:7.2.0-jdk8 AS build
WORKDIR /usr/app/
COPY . .
ARG USERNAME
ENV USERNAME=$USERNAME
ARG TOKEN
ENV TOKEN=$TOKEN
RUN gradle shadowJar

FROM openjdk:8-jre-alpine
WORKDIR /usr/app/
ENV MEMORY="1G"
COPY --from=build /usr/app/build/libs/Hydra-all.jar /usr/app/Hydra.jar
ENTRYPOINT java -Xmx${MEMORY} -jar Hydra.jar
