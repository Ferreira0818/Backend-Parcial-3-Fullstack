# Etapa 1: Compilación
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copiar POM raíz y POM del módulo
COPY pom.xml ./pom.xml
COPY modulo-eureka/pom.xml ./modulo-eureka/pom.xml

# Descargar dependencias
RUN mvn -B -f modulo-eureka/pom.xml dependency:go-offline

# Copiar código fuente y compilar
COPY modulo-eureka/src ./modulo-eureka/src
RUN mvn -B -f modulo-eureka/pom.xml clean package -DskipTests

# Etapa 2: Imagen final de ejecución
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Instalar curl para healthchecks de docker-compose
RUN apk add --no-cache curl

COPY --from=build /app/modulo-eureka/target/*.jar app.jar

EXPOSE 8761

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
