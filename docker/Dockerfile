# Build Application Jar
FROM gradle:7.4.2-jdk8 AS build

WORKDIR /usr/app/

# Copy Hydra project files
COPY . .

RUN gradle shadowJar

# Run Application
FROM openjdk:18.0.2.1-jdk

WORKDIR /usr/app/

# Get all environments variables
ENV MEMORY="1G"

# Copy previous builded Jar
COPY --from=build /usr/app/build/libs/Hydra-all.jar /usr/app/Hydra.jar

ENTRYPOINT java -Xmx${MEMORY} -jar Hydra.jar