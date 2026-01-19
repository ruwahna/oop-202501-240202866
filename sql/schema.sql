-- ============================================
-- Agri-POS Database Schema
-- PostgreSQL Database
-- ============================================

-- Drop tables if exist (untuk reset)
DROP TABLE IF EXISTS transaction_items CASCADE;
DROP TABLE IF EXISTS transactions CASCADE;
DROP TABLE IF EXISTS products CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- ============================================
-- Tabel Users (untuk autentikasi)
-- ============================================
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'KASIR',
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- Tabel Products (produk pertanian)
-- ============================================
CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    code VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    price DECIMAL(12,2) NOT NULL,
    stock INTEGER NOT NULL DEFAULT 0,
    unit VARCHAR(20) NOT NULL DEFAULT 'kg',
    description TEXT,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- Tabel Transactions (transaksi penjualan)
-- ============================================
CREATE TABLE transactions (
    id SERIAL PRIMARY KEY,
    transaction_code VARCHAR(30) UNIQUE NOT NULL,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    cashier_username VARCHAR(50),
    user_id INTEGER REFERENCES users(id),
    subtotal DECIMAL(15,2) NOT NULL DEFAULT 0,
    tax DECIMAL(15,2) NOT NULL DEFAULT 0,
    total DECIMAL(15,2) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    amount_paid DECIMAL(15,2) NOT NULL,
    change_amount DECIMAL(15,2) NOT NULL DEFAULT 0,
    status VARCHAR(20) DEFAULT 'COMPLETED',
    notes TEXT
);

-- ============================================
-- Tabel Transaction Items (detail transaksi)
-- ============================================
CREATE TABLE transaction_items (
    id SERIAL PRIMARY KEY,
    transaction_id INTEGER REFERENCES transactions(id) ON DELETE CASCADE,
    product_id INTEGER REFERENCES products(id),
    product_code VARCHAR(20) NOT NULL,
    product_name VARCHAR(100) NOT NULL,
    quantity INTEGER NOT NULL,
    unit_price DECIMAL(12,2) NOT NULL,
    subtotal DECIMAL(15,2) NOT NULL
);

-- ============================================
-- Indexes untuk performa
-- ============================================
CREATE INDEX idx_products_code ON products(code);
CREATE INDEX idx_products_category ON products(category);
CREATE INDEX idx_transactions_code ON transactions(transaction_code);
CREATE INDEX idx_transactions_date ON transactions(transaction_date);
CREATE INDEX idx_transactions_user ON transactions(user_id);

-- ============================================
-- Trigger untuk update timestamp
-- ============================================
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_products_updated_at
    BEFORE UPDATE ON products
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();
