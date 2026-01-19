-- =============================================================
-- AGRI-POS SEED DATA
-- Data awal untuk pengujian sistem
-- =============================================================

-- =============================================================
-- SEED DATA: users
-- Password: admin123 untuk admin, kasir123 untuk kasir
-- (dalam produksi sebaiknya di-hash)
-- =============================================================
INSERT INTO users (username, password, role) VALUES
    ('admin', 'admin123', 'ADMIN'),
    ('kasir1', 'kasir123', 'KASIR'),
    ('kasir2', 'kasir123', 'KASIR'),
    ('supervisor', 'super123', 'ADMIN');

-- =============================================================
-- SEED DATA: products
-- Produk pertanian dengan berbagai kategori
-- =============================================================
INSERT INTO products (code, name, category, price, stock) VALUES
    -- Kategori Beras
    ('BRS001', 'Beras Premium 5kg', 'Beras', 65000.00, 100),
    ('BRS002', 'Beras Medium 5kg', 'Beras', 55000.00, 150),
    ('BRS003', 'Beras Ketan 1kg', 'Beras', 22000.00, 50),
    ('BRS004', 'Beras Merah 1kg', 'Beras', 28000.00, 40),
    ('BRS005', 'Beras Organik 5kg', 'Beras', 85000.00, 30),
    
    -- Kategori Gula & Minyak
    ('GLM001', 'Gula Pasir 1kg', 'Gula & Minyak', 14000.00, 200),
    ('GLM002', 'Gula Merah 500g', 'Gula & Minyak', 12000.00, 80),
    ('GLM003', 'Minyak Goreng 2L', 'Gula & Minyak', 28000.00, 120),
    ('GLM004', 'Minyak Kelapa 1L', 'Gula & Minyak', 35000.00, 50),
    
    -- Kategori Bumbu Dapur
    ('BMB001', 'Bawang Merah 250g', 'Bumbu Dapur', 15000.00, 100),
    ('BMB002', 'Bawang Putih 250g', 'Bumbu Dapur', 18000.00, 100),
    ('BMB003', 'Cabai Merah 250g', 'Bumbu Dapur', 20000.00, 80),
    ('BMB004', 'Jahe 100g', 'Bumbu Dapur', 5000.00, 150),
    ('BMB005', 'Kunyit 100g', 'Bumbu Dapur', 4000.00, 120),
    ('BMB006', 'Lengkuas 100g', 'Bumbu Dapur', 3500.00, 100),
    
    -- Kategori Sayuran
    ('SAY001', 'Bayam 250g', 'Sayuran', 5000.00, 60),
    ('SAY002', 'Kangkung 250g', 'Sayuran', 4000.00, 70),
    ('SAY003', 'Wortel 500g', 'Sayuran', 8000.00, 50),
    ('SAY004', 'Kentang 1kg', 'Sayuran', 15000.00, 80),
    ('SAY005', 'Tomat 500g', 'Sayuran', 10000.00, 90),
    ('SAY006', 'Kubis 1kg', 'Sayuran', 12000.00, 40),
    
    -- Kategori Buah-buahan
    ('BUH001', 'Pisang Raja 1sisir', 'Buah-buahan', 18000.00, 50),
    ('BUH002', 'Jeruk Manis 1kg', 'Buah-buahan', 25000.00, 60),
    ('BUH003', 'Apel Fuji 1kg', 'Buah-buahan', 45000.00, 40),
    ('BUH004', 'Mangga Harum Manis 1kg', 'Buah-buahan', 30000.00, 35),
    ('BUH005', 'Pepaya 1kg', 'Buah-buahan', 12000.00, 45),
    
    -- Kategori Pupuk & Pestisida
    ('PUP001', 'Pupuk NPK 5kg', 'Pupuk & Pestisida', 75000.00, 50),
    ('PUP002', 'Pupuk Urea 5kg', 'Pupuk & Pestisida', 55000.00, 60),
    ('PUP003', 'Pupuk Organik 5kg', 'Pupuk & Pestisida', 45000.00, 70),
    ('PUP004', 'Pestisida Organik 500ml', 'Pupuk & Pestisida', 35000.00, 40),
    
    -- Kategori Bibit & Benih
    ('BBT001', 'Bibit Padi Unggul 5kg', 'Bibit & Benih', 120000.00, 30),
    ('BBT002', 'Benih Cabai 100g', 'Bibit & Benih', 25000.00, 50),
    ('BBT003', 'Benih Tomat 50g', 'Bibit & Benih', 20000.00, 50),
    ('BBT004', 'Benih Bayam 100g', 'Bibit & Benih', 15000.00, 60),
    ('BBT005', 'Bibit Jeruk', 'Bibit & Benih', 35000.00, 25);

-- =============================================================
-- SEED DATA: transactions (Sample transactions)
-- =============================================================
INSERT INTO transactions (transaction_code, user_id, subtotal, tax, total, payment_method, amount_paid, change_amount, status, transaction_date) VALUES
    ('TRX-20240115-001', 2, 200000.00, 20000.00, 220000.00, 'CASH', 250000.00, 30000.00, 'COMPLETED', '2024-01-15 09:30:00'),
    ('TRX-20240115-002', 2, 150000.00, 15000.00, 165000.00, 'E-WALLET', 165000.00, 0.00, 'COMPLETED', '2024-01-15 10:45:00'),
    ('TRX-20240115-003', 3, 300000.00, 30000.00, 330000.00, 'CASH', 350000.00, 20000.00, 'COMPLETED', '2024-01-15 14:20:00'),
    ('TRX-20240116-001', 2, 450000.00, 45000.00, 495000.00, 'E-WALLET', 495000.00, 0.00, 'COMPLETED', '2024-01-16 08:15:00'),
    ('TRX-20240116-002', 3, 180000.00, 18000.00, 198000.00, 'CASH', 200000.00, 2000.00, 'COMPLETED', '2024-01-16 11:30:00');

-- =============================================================
-- SEED DATA: transaction_items (Items for sample transactions)
-- =============================================================
-- Transaction 1 items
INSERT INTO transaction_items (transaction_id, product_code, product_name, unit_price, quantity, subtotal) VALUES
    (1, 'BRS001', 'Beras Premium 5kg', 65000.00, 2, 130000.00),
    (1, 'GLM001', 'Gula Pasir 1kg', 14000.00, 3, 42000.00),
    (1, 'GLM003', 'Minyak Goreng 2L', 28000.00, 1, 28000.00);

-- Transaction 2 items
INSERT INTO transaction_items (transaction_id, product_code, product_name, unit_price, quantity, subtotal) VALUES
    (2, 'BMB001', 'Bawang Merah 250g', 15000.00, 2, 30000.00),
    (2, 'BMB002', 'Bawang Putih 250g', 18000.00, 2, 36000.00),
    (2, 'BMB003', 'Cabai Merah 250g', 20000.00, 2, 40000.00),
    (2, 'SAY005', 'Tomat 500g', 10000.00, 2, 20000.00),
    (2, 'SAY001', 'Bayam 250g', 5000.00, 2, 10000.00),
    (2, 'BMB004', 'Jahe 100g', 5000.00, 2, 10000.00),
    (2, 'BMB005', 'Kunyit 100g', 4000.00, 1, 4000.00);

-- Transaction 3 items
INSERT INTO transaction_items (transaction_id, product_code, product_name, unit_price, quantity, subtotal) VALUES
    (3, 'PUP001', 'Pupuk NPK 5kg', 75000.00, 2, 150000.00),
    (3, 'PUP002', 'Pupuk Urea 5kg', 55000.00, 2, 110000.00),
    (3, 'PUP004', 'Pestisida Organik 500ml', 35000.00, 1, 35000.00),
    (3, 'SAY001', 'Bayam 250g', 5000.00, 1, 5000.00);

-- Transaction 4 items
INSERT INTO transaction_items (transaction_id, product_code, product_name, unit_price, quantity, subtotal) VALUES
    (4, 'BBT001', 'Bibit Padi Unggul 5kg', 120000.00, 3, 360000.00),
    (4, 'BBT002', 'Benih Cabai 100g', 25000.00, 2, 50000.00),
    (4, 'BBT003', 'Benih Tomat 50g', 20000.00, 2, 40000.00);

-- Transaction 5 items
INSERT INTO transaction_items (transaction_id, product_code, product_name, unit_price, quantity, subtotal) VALUES
    (5, 'BUH001', 'Pisang Raja 1sisir', 18000.00, 3, 54000.00),
    (5, 'BUH002', 'Jeruk Manis 1kg', 25000.00, 2, 50000.00),
    (5, 'BUH003', 'Apel Fuji 1kg', 45000.00, 1, 45000.00),
    (5, 'BUH005', 'Pepaya 1kg', 12000.00, 2, 24000.00),
    (5, 'SAY003', 'Wortel 500g', 8000.00, 1, 8000.00);

-- =============================================================
-- VERIFICATION QUERIES
-- =============================================================
-- Cek jumlah data
SELECT 'users' AS table_name, COUNT(*) AS row_count FROM users
UNION ALL
SELECT 'products', COUNT(*) FROM products
UNION ALL
SELECT 'transactions', COUNT(*) FROM transactions
UNION ALL
SELECT 'transaction_items', COUNT(*) FROM transaction_items;
