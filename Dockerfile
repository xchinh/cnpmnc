FROM maven:3.9.9-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/cnpmnc-0.0.1-SNAPSHOT.war cnpmnc.war
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "cnpmnc.war"]