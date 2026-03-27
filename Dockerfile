# Build stage
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

COPY . .

RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

ENV PORT=10000
EXPOSE 10000

ENTRYPOINT ["sh", "-c", "java -Dserver.port=$PORT -jar app.jar"]