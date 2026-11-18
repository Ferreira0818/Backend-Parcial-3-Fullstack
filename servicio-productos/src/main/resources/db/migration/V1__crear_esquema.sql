CREATE TABLE Categoria (
    IdCategoria     INT AUTO_INCREMENT PRIMARY KEY,
    NombreCategoria VARCHAR(80) NOT NULL UNIQUE
);

CREATE TABLE Producto (
    IdProducto     INT AUTO_INCREMENT PRIMARY KEY,
    Imagen         VARCHAR(255),
    Precio         DOUBLE NOT NULL CHECK (Precio >= 0),
    NombreProducto VARCHAR(120) NOT NULL UNIQUE,
    Alt            VARCHAR(150),
    Descripcion    VARCHAR(500),
    Stock          INT NOT NULL DEFAULT 0 CHECK (Stock >= 0),
    CategoriaId    INT NOT NULL,
    CONSTRAINT fk_producto_categoria
        FOREIGN KEY (CategoriaId) REFERENCES Categoria(IdCategoria)
);

-- Inserción de Categorías iniciales del universo Path of Exile
INSERT INTO Categoria (NombreCategoria) VALUES 
('Currency'),
('Fragments'),
('Divination Cards'),
('Unique Items');

-- Inserción de Productos iniciales (Orbes y objetos de PoE)
INSERT INTO Producto (Imagen, Precio, NombreProducto, Alt, Descripcion, Stock, CategoriaId) VALUES
('https://web.poecdn.com/gen/image/WzI1LDE0LHsiZiI6IjJESXRlbXMvQ3VycmVuY3kvQ3VycmVuY3lNb2RWYWx1ZXMiLCJ3IjoxLCJoIjoxLCJzY2FsZSI6MX1d/e1a54ff97d/CurrencyModValues.png', 1.85, 'Divine Orb', 'Orbe Divino de PoE', 'Re-rolls the values of all random explicit modifiers on an item.', 500, 1),
('https://web.poecdn.com/gen/image/WzI1LDE0LHsiZiI6IjJESXRlbXMvQ3VycmVuY3kvQ3VycmVuY3lSZXJvbGxSYW5kb20iLCJ3IjoxLCJoIjoxLCJzY2FsZSI6MX1d/5dac89f4eb/CurrencyRerollRandom.png', 0.05, 'Chaos Orb', 'Orbe del Caos de PoE', 'Reforges a rare item with new random modifiers.', 10000, 1),
('https://web.poecdn.com/gen/image/WzI1LDE0LHsiZiI6IjJESXRlbXMvQ3VycmVuY3kvQ3VycmVuY3lEdXBsaWNhdGUiLCJ3IjoxLCJoIjoxLCJzY2FsZSI6MX1d/51ec8f1211/CurrencyDuplicate.png', 280.00, 'Mirror of Kalandra', 'Espejo de Kalandra de PoE', 'Creates a mirrored copy of an equipable, non-unique item.', 15, 1),
('https://web.poecdn.com/gen/image/WzI1LDE0LHsiZiI6IjJESXRlbXMvQ3VycmVuY3kvQ3VycmVuY3lBZGRNb2RUb1JhcmUiLCJ3IjoxLCJoIjoxLCJzY2FsZSI6MX1d/b1c4b78635/CurrencyAddModToRare.png', 0.15, 'Exalted Orb', 'Orbe Exaltado de PoE', 'Enchants a rare item with a new random modifier.', 2500, 1),
('https://web.poecdn.com/gen/image/WzI1LDE0LHsiZiI6IjJESXRlbXMvQXJtb3Vycy9CZWx0cy9NYWdlYmxvb2QiLCJ3IjoyLCJoIjoxLCJzY2FsZSI6MX1d/2c83ff4bbf/Mageblood.png', 150.00, 'Mageblood Heavy Belt', 'Cinturón Mageblood Único', 'Magic Utility Flask effects cannot be removed and are permanently active.', 8, 4);
