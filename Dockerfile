# ── Stage 1: Build ───────────────────────────────────────────────────────────
FROM gradle:9.4.1-jdk17 AS builder

WORKDIR /app

# Copy gradle wrapper and dependency files first (layer caching)
COPY gradlew gradlew.bat ./
COPY gradle ./gradle
COPY build.gradle settings.gradle ./

# Download dependencies (cached unless build files change)
RUN gradle dependencies --no-daemon || true

# Copy source and build the fat JAR
COPY src ./src
RUN gradle bootJar --no-daemon

# ── Stage 2: Run ─────────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy only the executable JAR from builder
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 7800

ENTRYPOINT ["java", "-jar", "app.jar"]

