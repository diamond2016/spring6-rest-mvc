# --- Build Stage ---
# Use a base image with Maven and JDK to build the application
FROM eclipse-temurin:21-jdk as builder

WORKDIR /build

# Copy the Maven wrapper and pom.xml to leverage Docker layer caching
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy the rest of the source code and build the application
COPY src ./src
RUN ./mvnw package -DskipTests

# --- Run Stage ---
# Use a slim JRE image for the final application
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /build/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
