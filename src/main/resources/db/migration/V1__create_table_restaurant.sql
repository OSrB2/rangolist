CREATE TABLE tb_restaurant (
    id BIGSERIAL PRIMARY KEY,
    image VARCHAR(255),
    name VARCHAR(255) NOT NULL,
    opening_hours VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status BOOLEAN DEFAULT true,
    address_street VARCHAR(255),
    address_city VARCHAR(255),
    address_state VARCHAR(50),
    address_zip_code VARCHAR(20)
);