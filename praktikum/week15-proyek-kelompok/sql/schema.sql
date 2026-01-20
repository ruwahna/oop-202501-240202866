-- =============================================================
-- AGRI-POS DATABASE SCHEMA
-- Sistem Point of Sale Pertanian
-- =============================================================

-- Drop existing tables if any
DROP TABLE IF EXISTS transaction_items;
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS users;

-- =============================================================
-- TABEL: users
-- Menyimpan data pengguna sistem (kasir dan admin)
-- =============================================================
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('KASIR', 'ADMIN')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Index untuk pencarian username
CREATE INDEX idx_users_username ON users(username);

-- =============================================================
-- TABEL: products
-- Menyimpan data produk pertanian
-- =============================================================
CREATE TABLE products (
    code VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    price DECIMAL(12, 2) NOT NULL CHECK (price >= 0),
    stock INT NOT NULL DEFAULT 0 CHECK (stock >= 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Index untuk pencarian dan filter
CREATE INDEX idx_products_category ON products(category);
CREATE INDEX idx_products_name ON products(name);

-- =============================================================
-- TABEL: transactions
-- Menyimpan header transaksi penjualan
-- =============================================================
CREATE TABLE transactions (
    id SERIAL PRIMARY KEY,
    transaction_code VARCHAR(50) NOT NULL UNIQUE,
    user_id INT NOT NULL,
    subtotal DECIMAL(12, 2) NOT NULL,
    discount DECIMAL(12, 2) NOT NULL DEFAULT 0,
    tax DECIMAL(12, 2) NOT NULL,
    total DECIMAL(12, 2) NOT NULL,
    payment_method VARCHAR(30) NOT NULL,
    amount_paid DECIMAL(12, 2) NOT NULL,
    change_amount DECIMAL(12, 2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'COMPLETED' CHECK (status IN ('PENDING', 'COMPLETED', 'CANCELLED')),
    transaction_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_transactions_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Index untuk pencarian transaksi
CREATE INDEX idx_transactions_date ON transactions(transaction_date);
CREATE INDEX idx_transactions_code ON transactions(transaction_code);
CREATE INDEX idx_transactions_user ON transactions(user_id);

-- =============================================================
-- TABEL: transaction_items
-- Menyimpan detail item dalam transaksi
-- =============================================================
CREATE TABLE transaction_items (
    id SERIAL PRIMARY KEY,
    transaction_id INT NOT NULL,
    product_code VARCHAR(20) NOT NULL,
    product_name VARCHAR(100) NOT NULL,
    unit_price DECIMAL(12, 2) NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    subtotal DECIMAL(12, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_transaction_items_transaction FOREIGN KEY (transaction_id) REFERENCES transactions(id) ON DELETE CASCADE,
    CONSTRAINT fk_transaction_items_product FOREIGN KEY (product_code) REFERENCES products(code)
);

-- Index untuk join dan pencarian
CREATE INDEX idx_transaction_items_transaction ON transaction_items(transaction_id);
CREATE INDEX idx_transaction_items_product ON transaction_items(product_code);

-- =============================================================
-- VIEWS
-- =============================================================

-- View untuk laporan penjualan harian
CREATE OR REPLACE VIEW daily_sales_report AS
SELECT 
    DATE(t.transaction_date) AS sale_date,
    COUNT(t.id) AS total_transactions,
    SUM(t.total) AS total_sales,
    SUM(t.tax) AS total_tax,
    AVG(t.total) AS average_transaction
FROM transactions t
WHERE t.status = 'COMPLETED'
GROUP BY DATE(t.transaction_date)
ORDER BY sale_date DESC;

-- View untuk penjualan per produk
CREATE OR REPLACE VIEW product_sales_report AS
SELECT 
    ti.product_code,
    ti.product_name,
    SUM(ti.quantity) AS total_quantity_sold,
    SUM(ti.subtotal) AS total_revenue,
    AVG(ti.unit_price) AS average_price
FROM transaction_items ti
JOIN transactions t ON ti.transaction_id = t.id
WHERE t.status = 'COMPLETED'
GROUP BY ti.product_code, ti.product_name
ORDER BY total_revenue DESC;

-- View untuk stok rendah
CREATE OR REPLACE VIEW low_stock_products AS
SELECT 
    code,
    name,
    category,
    stock,
    price
FROM products
WHERE stock < 10
ORDER BY stock ASC;

-- =============================================================
-- FUNCTIONS (Optional - untuk trigger)
-- =============================================================

-- Function untuk update timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Triggers untuk auto-update timestamp
CREATE TRIGGER update_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_products_updated_at
    BEFORE UPDATE ON products
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- =============================================================
-- COMMENTS
-- =============================================================
COMMENT ON TABLE users IS 'Tabel pengguna sistem (kasir dan admin)';
COMMENT ON TABLE products IS 'Tabel produk pertanian';
COMMENT ON TABLE transactions IS 'Tabel header transaksi penjualan';
COMMENT ON TABLE transaction_items IS 'Tabel detail item transaksi';
