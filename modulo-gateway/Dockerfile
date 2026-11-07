# Etapa 1: Compilación
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copiar POM raíz y POM del módulo
COPY pom.xml ./pom.xml
COPY modulo-gateway/pom.xml ./modulo-gateway/pom.xml

# Descargar dependencias
RUN mvn -B -f modulo-gateway/pom.xml dependency:go-offline

# Copiar código fuente y compilar
COPY modulo-gateway/src ./modulo-gateway/src
RUN mvn -B -f modulo-gateway/pom.xml clean package -DskipTests

# Etapa 2: Imagen final de ejecución
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN apk add --no-cache curl

COPY --from=build /app/modulo-gateway/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
