# Etapa 1: Compilación
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml ./pom.xml
COPY servicio-transacciones/pom.xml ./servicio-transacciones/pom.xml

RUN mvn -B -f servicio-transacciones/pom.xml dependency:go-offline

COPY servicio-transacciones/src ./servicio-transacciones/src
RUN mvn -B -f servicio-transacciones/pom.xml clean package -DskipTests

# Etapa 2: Imagen de ejecución
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN apk add --no-cache curl

COPY --from=build /app/servicio-transacciones/target/*.jar app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
