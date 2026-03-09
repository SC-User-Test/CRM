# Multi-stage Dockerfile for CRM Spring Boot Application
# Builder stage: Maven with Eclipse Temurin JDK 8
FROM maven:3.9.4-eclipse-temurin-8 AS builder

# Set working directory
WORKDIR /workspace

# Copy pom.xml first for dependency caching
COPY pom.xml .

# Download dependencies (cached layer)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build application (skip tests for faster builds)
RUN mvn clean package -DskipTests

# Runtime stage: Amazon Corretto 8 (explicit base image)
FROM amazoncorretto:8

# Add metadata
LABEL maintainer="CRM Application Team"
LABEL description="CRM Spring Boot Application"
LABEL version="0.0.1-SNAPSHOT"

# Create application directory
WORKDIR /app

# Create non-root user for security
RUN yum install -y shadow-utils && \
    groupadd -r appuser && \
    useradd -r -g appuser appuser && \
    yum clean all

# Copy JAR from builder stage
COPY --from=builder /workspace/target/*.jar app.jar

# Change ownership to non-root user
RUN chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Expose application port
EXPOSE 8080

# Set JVM options for containerized environment
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

# Set timezone
ENV TZ=UTC

# Run application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]