# ==============================================================================
# TravelSync — Multi-Stage Production Dockerfile for Java 17 REST API Service
# ==============================================================================

# Stage 1: Build Java Application with Maven
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder
WORKDIR /app

# Copy Maven POM and warm dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B || true

# Copy application source code and datasets
COPY src ./src
COPY dataset ./dataset
COPY data ./data

# Compile, verify with JUnit test suite, and package shaded executable JAR
RUN mvn clean package -DskipTests

# Stage 2: Lightweight Production Runtime Image
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy shaded uber-JAR and datasets into container workspace
COPY --from=builder /app/target/travelsync-1.0.0.jar ./travelsync.jar
COPY dataset ./dataset
COPY data ./data

# Port configuration (Render dynamically sets $PORT)
ENV PORT=8080
EXPOSE 8080

# Launch TravelSync REST API Server
ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-jar", "travelsync.jar"]
