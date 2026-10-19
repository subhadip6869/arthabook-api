# ---------- Build Stage ----------
FROM maven:3.9.9-eclipse-temurin-17-alpine AS builder

WORKDIR /app

# Cache Maven dependencies (only re-runs if pom.xml changes)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy application source code and compile JAR
COPY src ./src
RUN mvn clean package -DskipTests


# ---------- Runtime Stage ----------
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Create non-root user for container security
RUN addgroup -S spring && adduser -S -u 10014 spring -G spring

# Copy compiled JAR from build stage
COPY --from=builder /app/target/*.jar app.jar
RUN chown spring:spring app.jar
USER 10014

EXPOSE 8080

# Production container flags:
# - MaxRAMPercentage=75.0 limits heap to ~384MB on a 512MB machine
# - UseSerialGC removes multi-threaded GC memory overhead
# - Xss256k trims thread stack footprint
# - urandom prevents blocking during SSL/token crypto handshakes
ENTRYPOINT ["java", \
  "-XX:MaxRAMPercentage=75.0", \
  "-XX:+UseSerialGC", \
  "-Xss256k", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]