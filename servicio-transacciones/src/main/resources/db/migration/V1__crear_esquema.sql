CREATE TABLE Estado (
    IdEstado     INT AUTO_INCREMENT PRIMARY KEY,
    NombreEstado VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE MedioDePago (
    IdMedioDePago     INT AUTO_INCREMENT PRIMARY KEY,
    NombreMedioDePago VARCHAR(80) NOT NULL UNIQUE
);

CREATE TABLE Transaccion (
    IdTransaccion    INT AUTO_INCREMENT PRIMARY KEY,
    IdUsuario        INT NOT NULL, -- Referencia lógica a servicio-usuarios (sin FK física por patrón Database-per-Service)
    Total            DOUBLE NOT NULL DEFAULT 0, 
    IdEstado         INT NOT NULL,
    FechaTransaccion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    IdMedioDePago    INT NULL,     -- Permite NULL mientras la transacción esté en estado 'EN_CARRITO'
    CONSTRAINT fk_transaccion_estado
        FOREIGN KEY (IdEstado)      REFERENCES Estado(IdEstado),
    CONSTRAINT fk_transaccion_medio
        FOREIGN KEY (IdMedioDePago) REFERENCES MedioDePago(IdMedioDePago)
);

CREATE TABLE TransaccionProducto (
    TransaccionId    INT NOT NULL,
    ProductoId       INT NOT NULL, -- Referencia lógica a servicio-productos (sin FK física por patrón Database-per-Service)
    PrecioProducto   DOUBLE NOT NULL, 
    CantidadProducto INT NOT NULL CHECK (CantidadProducto > 0),
    PRIMARY KEY (TransaccionId, ProductoId),
    CONSTRAINT fk_tp_transaccion
        FOREIGN KEY (TransaccionId) REFERENCES Transaccion(IdTransaccion)
        ON DELETE CASCADE
);

-- Inserción de Estados del ciclo de vida del Carrito/Transacción
INSERT INTO Estado (NombreEstado) VALUES 
('EN_CARRITO'),
('PAGADO'),
('CANCELADO');

-- Inserción de Medios de Pago iniciales
INSERT INTO MedioDePago (NombreMedioDePago) VALUES 
('TARJETA_CREDITO'),
('PAYPAL'),
('CRIPTOMONEDA'),
('TRANSFERENCIA_BANCARIA');
