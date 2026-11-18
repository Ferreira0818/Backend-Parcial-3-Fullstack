CREATE TABLE Rol (
    IdRol     INT AUTO_INCREMENT PRIMARY KEY,
    NombreRol VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE Usuario (
    IdUsuario     INT AUTO_INCREMENT PRIMARY KEY,
    NombreUsuario VARCHAR(100) NOT NULL,
    Contrasena    VARCHAR(255) NOT NULL, 
    Email         VARCHAR(150) NOT NULL UNIQUE,
    FechaRegistro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    IdRol         INT NOT NULL,
    CONSTRAINT fk_usuario_rol
        FOREIGN KEY (IdRol) REFERENCES Rol(IdRol)
);

-- Inserción de roles iniciales
INSERT INTO Rol (NombreRol) VALUES ('ADMIN'), ('CLIENTE');
