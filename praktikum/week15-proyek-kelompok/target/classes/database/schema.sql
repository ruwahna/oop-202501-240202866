-- Hapus tabel jika sudah ada (untuk reset)
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS transaction_items;
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS users;

-- 1. Tabel Users (FR-5)
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL -- 'Admin' atau 'Kasir'
);

-- 2. Tabel Products (FR-1)
CREATE TABLE products (
    kode VARCHAR(20) PRIMARY KEY,
    nama VARCHAR(100) NOT NULL,
    kategori VARCHAR(50),
    harga DECIMAL(12, 2) NOT NULL,
    stok INT NOT NULL
);

-- 3. Tabel Transactions (FR-2)
CREATE TABLE transactions (
    id SERIAL PRIMARY KEY,
    tanggal TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(12, 2) NOT NULL,
    status VARCHAR(20) DEFAULT 'SUCCESS',
    metode_pembayaran VARCHAR(50) -- 'Tunai', 'E-Wallet', 'QRIS', dll
);

-- 4. Tabel Transaction Items (Detail Belanja)
CREATE TABLE transaction_items (
    id SERIAL PRIMARY KEY,
    transaction_id INT REFERENCES transactions(id) ON DELETE CASCADE,
    product_kode VARCHAR(20) REFERENCES products(kode),
    qty INT NOT NULL,
    subtotal DECIMAL(12, 2) NOT NULL
);

-- 5. Tabel Payments (FR-3)
CREATE TABLE payments (
    id SERIAL PRIMARY KEY,
    transaction_id INT REFERENCES transactions(id),
    metode VARCHAR(20) NOT NULL, -- 'TUNAI' atau 'E-WALLET'
    jumlah_bayar DECIMAL(12, 2) NOT NULL,
    kembalian DECIMAL(12, 2) NOT NULL
);