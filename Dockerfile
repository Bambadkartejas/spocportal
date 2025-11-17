# ---------- BUILD STAGE ----------
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom and download dependencies first (faster build)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Now copy source and build
COPY src ./src
RUN mvn clean package -DskipTests


# ---------- RUN STAGE ----------
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Render will inject the PORT environment variable
ENV PORT=10000
EXPOSE 10000

# Start Spring Boot
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT}"]
