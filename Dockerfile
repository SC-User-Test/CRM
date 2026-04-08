# Multi-stage Dockerfile for Cloud Deployment
# Compatible with AWS ECS, Azure Container Instances, GCP Cloud Run

# Stage 1: Build
FROM maven:3.6.3-jdk-8-alpine AS build
WORKDIR /app

# Copy pom.xml and download dependencies (cached layer)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM openjdk:8-jre-alpine

# Add metadata
LABEL maintainer="CompTestCRM Team"
LABEL description="Cloud-ready CRM application"
LABEL version="0.0.1-SNAPSHOT"

# Create non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring

# Create directories for persistent storage
RUN mkdir -p /tmp/pdfs /tmp/csv /tmp/uploads && \
    chown -R spring:spring /tmp/pdfs /tmp/csv /tmp/uploads

# Set working directory
WORKDIR /app

# Copy JAR from build stage
COPY --from=build /app/target/crm-0.0.1-SNAPSHOT.jar app.jar

# Change ownership to non-root user
RUN chown spring:spring app.jar

# Switch to non-root user
USER spring:spring

# Expose application port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:8080/appinfo/health || exit 1

# JVM options for cloud environments
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -Djava.security.egd=file:/dev/./urandom"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
