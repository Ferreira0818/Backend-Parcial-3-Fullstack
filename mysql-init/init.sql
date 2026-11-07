-- Script de inicialización de esquemas para MySQL
-- Patrón Database-per-Service: 3 esquemas independientes

CREATE DATABASE IF NOT EXISTS db_usuarios CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS db_productos CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS db_transacciones CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

GRANT ALL PRIVILEGES ON db_usuarios.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON db_productos.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON db_transacciones.* TO 'root'@'%';
FLUSH PRIVILEGES;
