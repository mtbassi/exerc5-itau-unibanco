CREATE TABLE produto
(
    id        BINARY(16) PRIMARY KEY,
    nome      VARCHAR(255)   NOT NULL,
    preco     DECIMAL(10, 2) NOT NULL,
    categoria VARCHAR(255)   NOT NULL
);
