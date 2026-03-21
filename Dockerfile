# ====== ETAPA 1: BUILD ======
FROM maven:3.8.6-openjdk-11-slim AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package

# ====== ETAPA 2: RUNTIME ======
FROM eclipse-temurin:11-jre

WORKDIR /app

# Copiar los JARs desde build
COPY --from=build /app/target/producer-jar-with-dependencies.jar /app/producer.jar
COPY --from=build /app/target/consumer-jar-with-dependencies.jar /app/consumer.jar

VOLUME /app/config

ENTRYPOINT ["java", "-jar"]
CMD ["producer.jar"]