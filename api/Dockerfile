# ----------- Stage 1: Builder -----------
FROM openjdk:21-jdk-slim AS builder

WORKDIR /app

# Install required packages
RUN apt-get update && apt-get install -y findutils

# Copy Gradle wrapper & root config
COPY gradlew .
COPY settings.gradle .
COPY build.gradle .
COPY gradle gradle
COPY api/build.gradle api/build.gradle
COPY api/src api/src
COPY db/build.gradle db/build.gradle
COPY db/src db/src

RUN chmod +x gradlew
RUN ./gradlew :api:bootJar -x test

# ----------- Stage 2: Runtime -----------
FROM openjdk:21-jdk-slim

WORKDIR /app
COPY --from=builder /app/api/build/libs/*.jar app.jar
CMD ["java", "-jar", "app.jar"]
