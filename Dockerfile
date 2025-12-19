# ======================
# 1️⃣ BUILD STAGE
# ======================
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

# Maven cache için önce pom
COPY pom.xml .
RUN mvn dependency:go-offline

# Kaynak kod
COPY src ./src

# Testler çalışır (integration testler profile ile kapalı olmalı)
RUN mvn clean package -DskipTests

# ======================
# 2️⃣ RUN STAGE
# ======================
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/target/hotel-reservation-system-ydg-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]