# ---------- Build Stage ----------
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests


# ---------- Runtime Stage ----------
FROM eclipse-temurin:21-jre

WORKDIR /app

RUN useradd -u 10014 -m springuser
COPY --from=builder /app/target/*.jar app.jar
RUN chown springuser:springuser app.jar
USER 10014

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]