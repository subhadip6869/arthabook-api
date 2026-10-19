# ---------- Build Stage ----------
FROM maven:3.9.9-eclipse-temurin-17-alpine AS builder

WORKDIR /app

# Cache dependencies and Maven plugins
COPY pom.xml .
RUN mvn dependency:resolve-plugins dependency:resolve -B

# Compile without redundant clean using parallel threads
COPY src ./src
RUN mvn package -DskipTests -T 1C -B

# Extract Spring Boot 4 layers using the modern jarmode tools
RUN java -Djarmode=tools -jar target/*.jar extract --layers --destination extracted


# ---------- Runtime Stage ----------
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Create non-root user and group with explicit matching IDs
RUN addgroup -g 10014 -S spring && adduser -u 10014 -S spring -G spring

# Copy extracted layers directly with ownership
COPY --chown=10014:10014 --from=builder /app/extracted/dependencies/ ./
COPY --chown=10014:10014 --from=builder /app/extracted/spring-boot-loader/ ./
COPY --chown=10014:10014 --from=builder /app/extracted/snapshot-dependencies/ ./
COPY --chown=10014:10014 --from=builder /app/extracted/application/ ./

USER 10014

EXPOSE 8080

# Production runtime flags for Render:
# - TieredStopAtLevel=1: stops JIT at C1 client compiler to avoid startup CPU starvation
# - CICompilerCount=2: caps compiler thread count on shared host CPUs
# - MaxRAMPercentage=75.0: caps heap at ~384MB on a 512MB RAM instance
# - UseSerialGC: removes multi-threaded GC memory/CPU overhead
# - Xss256k: reduces stack memory footprint per thread
ENTRYPOINT ["java", \
  "-XX:TieredStopAtLevel=1", \
  "-XX:CICompilerCount=2", \
  "-XX:MaxRAMPercentage=75.0", \
  "-XX:+UseSerialGC", \
  "-Xss256k", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "org.springframework.boot.loader.launch.JarLauncher"]