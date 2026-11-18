# Etapa 1: Compilación
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml ./pom.xml
COPY servicio-productos/pom.xml ./servicio-productos/pom.xml

RUN mvn -B -f servicio-productos/pom.xml dependency:go-offline

COPY servicio-productos/src ./servicio-productos/src
RUN mvn -B -f servicio-productos/pom.xml clean package -DskipTests

# Etapa 2: Imagen de ejecución
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN apk add --no-cache curl

COPY --from=build /app/servicio-productos/target/*.jar app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
