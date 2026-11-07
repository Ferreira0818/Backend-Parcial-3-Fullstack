@echo off
chcp 65001 > nul
title PoE E-Commerce - Arranque de Microservicios

echo ===============================================================================
echo            PATH OF EXILE E-COMMERCE - ARQUITECTURA DE MICROSERVICIOS
echo ===============================================================================
echo.
echo [1/3] Verificando Docker y entorno...
docker --version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo [ADVERTENCIA] Docker no se encuentra instalado o no esta en el PATH.
    echo Asegurate de tener Docker Desktop iniciado para desplegar con contenedores.
) else (
    echo Docker detectado correctamente.
)
echo.

echo ===============================================================================
echo SELECCIONA EL MODO DE ARRANQUE:
echo 1. Despliegue completo con Docker Compose (Recomendado - Eureka, Gateway, MySQL y Servicios)
echo 2. Compilar con Maven (mvn clean package -DskipTests)
echo 3. Salir
echo ===============================================================================
set /p MODO="Ingresa tu opcion (1, 2 o 3): "

if "%MODO%"=="1" (
    echo.
    echo ===============================================================================
    echo Levantando la infraestructura con Docker Compose...
    echo ===============================================================================
    docker compose down
    docker compose up --build -d
    
    echo.
    echo Esperando a que Eureka Server inicialice (puerto 8761)...
    timeout /t 15 /nobreak > nul
    
    echo Esperando a que API Gateway inicialice (puerto 8080)...
    timeout /t 15 /nobreak > nul
    
    echo.
    echo ===============================================================================
    echo Estado de los Contenedores:
    docker compose ps
    echo ===============================================================================
    echo.
    echo Acceso a paneles:
    echo  - Eureka Dashboard: http://localhost:8761
    echo  - Gateway / Swagger UI: http://localhost:8080/swagger-ui.html
    echo  - Gateway API: http://localhost:8080/api/v1/...
    echo.
    pause
    exit /b 0
)

if "%MODO%"=="2" (
    echo.
    echo ===============================================================================
    echo Compilando todos los modulos con Maven...
    echo ===============================================================================
    call mvn clean package -DskipTests
    if %ERRORLEVEL% NEQ 0 (
        echo [ERROR] La compilacion ha fallado. Revisa los logs.
    ) else (
        echo [EXITO] Compilacion completada con exito.
    )
    pause
    exit /b 0
)

echo Saliendo...
exit /b 0
