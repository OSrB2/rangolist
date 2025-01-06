CREATE TABLE tb_promotions (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    description VARCHAR(255),
    promo_price DECIMAL(10, 2),
    promo_days VARCHAR(255),
    promo_hours VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status BOOLEAN DEFAULT true,
    -- Definindo a chave estrangeira para relacionar com a tabela 'products'
    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);
