-- ============================================
-- Agri-POS Seed Data
-- Data awal untuk testing
-- ============================================

-- ============================================
-- Users (password: plain text untuk demo)
-- Sesuai Use Case Diagram Week 6:
-- - KASIR: Menangani transaksi checkout dan cetak struk
-- - ADMIN: Mengelola data akun pengguna, produk, dan memantau laporan
-- ============================================
INSERT INTO users (username, password, full_name, role) VALUES
('admin', 'admin123', 'Administrator', 'ADMIN'),
('kasir1', 'kasir123', 'Budi Santoso', 'KASIR'),
('kasir2', 'kasir123', 'Siti Rahayu', 'KASIR');

-- ============================================
-- Products (Produk Pertanian)
-- ============================================
INSERT INTO products (code, name, category, price, stock, unit, description) VALUES
-- Sayuran
('SAY001', 'Beras Premium', 'Beras', 15000.00, 100, 'kg', 'Beras premium kualitas terbaik'),
('SAY002', 'Beras Medium', 'Beras', 12000.00, 150, 'kg', 'Beras medium kualitas baik'),
('SAY003', 'Cabai Merah', 'Sayuran', 45000.00, 50, 'kg', 'Cabai merah segar'),
('SAY004', 'Cabai Rawit', 'Sayuran', 55000.00, 40, 'kg', 'Cabai rawit pedas'),
('SAY005', 'Bawang Merah', 'Sayuran', 35000.00, 80, 'kg', 'Bawang merah lokal'),
('SAY006', 'Bawang Putih', 'Sayuran', 40000.00, 60, 'kg', 'Bawang putih import'),
('SAY007', 'Tomat', 'Sayuran', 12000.00, 100, 'kg', 'Tomat segar'),
('SAY008', 'Kentang', 'Sayuran', 18000.00, 70, 'kg', 'Kentang dieng'),
('SAY009', 'Wortel', 'Sayuran', 15000.00, 60, 'kg', 'Wortel segar'),
('SAY010', 'Kangkung', 'Sayuran', 5000.00, 100, 'ikat', 'Kangkung segar'),

-- Buah
('BUA001', 'Apel Malang', 'Buah', 25000.00, 50, 'kg', 'Apel malang manis'),
('BUA002', 'Jeruk Medan', 'Buah', 20000.00, 60, 'kg', 'Jeruk medan segar'),
('BUA003', 'Pisang Ambon', 'Buah', 18000.00, 80, 'sisir', 'Pisang ambon matang'),
('BUA004', 'Mangga Harumanis', 'Buah', 30000.00, 40, 'kg', 'Mangga harumanis manis'),
('BUA005', 'Semangka', 'Buah', 8000.00, 30, 'kg', 'Semangka merah manis'),

-- Pupuk
('PUP001', 'Pupuk Urea', 'Pupuk', 120000.00, 200, 'sak', 'Pupuk urea 50kg'),
('PUP002', 'Pupuk NPK', 'Pupuk', 150000.00, 150, 'sak', 'Pupuk NPK 50kg'),
('PUP003', 'Pupuk Organik', 'Pupuk', 80000.00, 100, 'sak', 'Pupuk organik alami'),
('PUP004', 'Pupuk Kompos', 'Pupuk', 50000.00, 120, 'sak', 'Pupuk kompos 25kg'),

-- Bibit
('BIB001', 'Bibit Padi', 'Bibit', 75000.00, 100, 'kg', 'Bibit padi unggul'),
('BIB002', 'Bibit Jagung', 'Bibit', 65000.00, 80, 'kg', 'Bibit jagung hibrida'),
('BIB003', 'Bibit Cabai', 'Bibit', 25000.00, 200, 'pack', 'Bibit cabai 100 biji'),
('BIB004', 'Bibit Tomat', 'Bibit', 20000.00, 150, 'pack', 'Bibit tomat 100 biji'),

-- Alat Pertanian
('ALT001', 'Cangkul', 'Alat', 85000.00, 30, 'pcs', 'Cangkul baja'),
('ALT002', 'Sabit', 'Alat', 45000.00, 40, 'pcs', 'Sabit panen'),
('ALT003', 'Sprayer Manual', 'Alat', 250000.00, 20, 'pcs', 'Sprayer manual 16L'),
('ALT004', 'Selang Air', 'Alat', 150000.00, 25, 'roll', 'Selang air 50m');

-- ============================================
-- Sample Transactions (untuk testing report)
-- ============================================
INSERT INTO transactions (transaction_code, user_id, total_amount, payment_method, payment_amount, change_amount, transaction_date) VALUES
('TRX-20260118-001', 1, 150000.00, 'CASH', 200000.00, 50000.00, '2026-01-18 08:30:00'),
('TRX-20260118-002', 2, 275000.00, 'CASH', 300000.00, 25000.00, '2026-01-18 09:15:00'),
('TRX-20260118-003', 1, 89000.00, 'OVO', 89000.00, 0.00, '2026-01-18 10:00:00');

-- Transaction Items for TRX-20260118-001
INSERT INTO transaction_items (transaction_id, product_id, product_code, product_name, quantity, unit_price, subtotal) VALUES
(1, 1, 'SAY001', 'Beras Premium', 5, 15000.00, 75000.00),
(1, 3, 'SAY003', 'Cabai Merah', 1, 45000.00, 45000.00),
(1, 5, 'SAY005', 'Bawang Merah', 1, 35000.00, 35000.00);

-- Transaction Items for TRX-20260118-002
INSERT INTO transaction_items (transaction_id, product_id, product_code, product_name, quantity, unit_price, subtotal) VALUES
(2, 16, 'PUP001', 'Pupuk Urea', 2, 120000.00, 240000.00),
(2, 5, 'SAY005', 'Bawang Merah', 1, 35000.00, 35000.00);

-- Transaction Items for TRX-20260118-003
INSERT INTO transaction_items (transaction_id, product_id, product_code, product_name, quantity, unit_price, subtotal) VALUES
(3, 11, 'BUA001', 'Apel Malang', 2, 25000.00, 50000.00),
(3, 12, 'BUA002', 'Jeruk Medan', 1, 20000.00, 20000.00),
(3, 13, 'BUA003', 'Pisang Ambon', 1, 18000.00, 18000.00);
