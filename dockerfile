FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/authservice-0.0.1-SNAPSHOT.jar authservice.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","authservice.jar"]
