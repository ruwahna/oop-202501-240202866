-- COPY SEMUA INI, PASTE DI PGADMIN, TEKAN F5
-- Ini akan reset tabel transactions dan transaction_items

DROP TABLE IF EXISTS transaction_items CASCADE;
DROP TABLE IF EXISTS transactions CASCADE;

CREATE TABLE transactions (
    id SERIAL PRIMARY KEY,
    transaction_code VARCHAR(30) UNIQUE NOT NULL,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    cashier_username VARCHAR(50),
    user_id INTEGER,
    subtotal DECIMAL(15,2) NOT NULL DEFAULT 0,
    tax DECIMAL(15,2) NOT NULL DEFAULT 0,
    total DECIMAL(15,2) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    amount_paid DECIMAL(15,2) NOT NULL,
    change_amount DECIMAL(15,2) NOT NULL DEFAULT 0,
    status VARCHAR(20) DEFAULT 'COMPLETED',
    notes TEXT
);

CREATE TABLE transaction_items (
    id SERIAL PRIMARY KEY,
    transaction_id INTEGER REFERENCES transactions(id) ON DELETE CASCADE,
    product_id INTEGER,
    product_code VARCHAR(20) NOT NULL,
    product_name VARCHAR(100) NOT NULL,
    quantity INTEGER NOT NULL,
    unit_price DECIMAL(12,2) NOT NULL,
    subtotal DECIMAL(15,2) NOT NULL
);
