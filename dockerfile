# Etapa 1: Build con Maven Wrapper y JDK 17
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app
COPY . .

# Compila el proyecto y genera el JAR
RUN ./mvnw clean package -DskipTests

# Etapa 2: Imagen final ligera solo con el JAR
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copiamos el JAR desde la etapa de build
COPY --from=build /app/target/authservice-0.0.1-SNAPSHOT.jar authservice.jar

# Exponemos el puerto 8081
EXPOSE 8081

# Comando para iniciar la aplicación
ENTRYPOINT ["java","-jar","authservice.jar"]
