FROM maven:3.9.4-eclipse-temurin-8 AS builder

WORKDIR /workspace

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests -B

FROM amazoncorretto:8

WORKDIR /app

RUN yum install -y shadow-utils && \
    groupadd -r appuser && \
    useradd -r -g appuser -s /sbin/nologin appuser && \
    yum clean all

COPY --from=builder /workspace/target/*.jar app.jar

RUN chown -R appuser:appuser /app

USER appuser

EXPOSE 8080

ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

ENV TZ=UTC

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]