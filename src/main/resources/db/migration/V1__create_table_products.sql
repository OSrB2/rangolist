CREATE TABLE tb_products (
    id BIGSERIAL PRIMARY KEY,
    restaurant_id BIGINT NOT NULL,
    image VARCHAR(255),
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    category VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status BOOLEAN DEFAULT true,
    -- Definindo a chave estrangeira para relacionar com a tabela 'restaurants'
    CONSTRAINT fk_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants(id) ON DELETE CASCADE
);