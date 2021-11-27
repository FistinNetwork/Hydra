FROM gradle:7.2.0-jdk8 AS build
WORKDIR /usr/app/
COPY . .
ARG USERNAME
ENV USERNAME=$USERNAME
ARG TOKEN
ENV TOKEN=$TOKEN
RUN gradle shadowJar

FROM openjdk:16-slim
WORKDIR /usr/app/
ENV MEMORY="1G" \
   STACK_NAME="" \
   REDIS_HOST="127.0.0.1" REDIS_PORT=6379 REDIS_PASS="" \
   NETWORK_NAME=""
COPY --from=build /usr/app/build/libs/Hydra-all.jar /usr/app/Hydra.jar
ENTRYPOINT java -Xmx${MEMORY} -jar Hydra.jar