# ============================================
# Multi-stage Dockerfile for Spring Boot Backend
# ============================================

# ===== Build Stage =====
FROM eclipse-temurin:21-jdk-alpine AS builder

LABEL stage=builder

# Set working directory
WORKDIR /workspace

# Copy maven wrapper and configuration
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies (cached layer)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build the application
RUN ./mvnw clean package -DskipTests -B

# Extract layers for better caching
RUN mkdir -p target/dependency && \
    cd target/dependency && \
    jar -xf ../*.jar

# ===== Production Stage =====
FROM eclipse-temurin:21-jre-alpine

LABEL maintainer="navire-app"
LABEL description="Spring Boot Backend for Navire Application"

# Install curl for healthcheck and debugging
RUN apk add --no-cache curl tzdata

# Set timezone
ENV TZ=UTC

# Create non-root user for security
RUN addgroup -g 1001 -S spring && \
    adduser -u 1001 -S spring -G spring

# Set working directory
WORKDIR /app

# Copy extracted layers from builder for better caching
ARG DEPENDENCY=/workspace/target/dependency
COPY --from=builder --chown=spring:spring ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=builder --chown=spring:spring ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=builder --chown=spring:spring ${DEPENDENCY}/BOOT-INF/classes /app

# Switch to non-root user
USER spring:spring

# Expose application port
EXPOSE 8086

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8086/actuator/health || exit 1

# JVM Options for production
ENV JAVA_OPTS="-XX:+UseContainerSupport \
    -XX:MaxRAMPercentage=75.0 \
    -XX:+UseG1GC \
    -XX:+UseStringDeduplication \
    -XX:+OptimizeStringConcat \
    -Djava.security.egd=file:/dev/./urandom"

# Run application with optimized JVM settings
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -cp /app:/app/lib/* com.example.navire.NavireApplication"]
