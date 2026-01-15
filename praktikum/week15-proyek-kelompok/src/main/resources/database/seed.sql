-- Tambah User Default
INSERT INTO users (username, password, role) VALUES 
('admin', 'admin123', 'Admin'),
('kasir', 'kasir123', 'Kasir');

-- Tambah Produk Awal (Agri-POS)
INSERT INTO products (kode, nama, kategori, harga, stok) VALUES 
('P001', 'Pupuk Urea 5kg', 'Pupuk', 75000, 50),
('P002', 'Benih Padi Unggul', 'Benih', 45000, 100),
('P003', 'Cangkul Baja', 'Alat', 120000, 15),
('P004', 'Pestisida Cair 1L', 'Obat', 85000, 30);