package com.upb.agripos.dao;

import com.upb.agripos.model.Transaction;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface DAO untuk Transaction (FR-2 Transaksi, FR-4 Laporan)
 * Menerapkan DIP - Dependency Inversion Principle
 */
public interface TransactionDAO {
    
    /**
     * Menyimpan transaksi baru beserta itemnya
     * @param transaction transaksi yang akan disimpan
     * @return ID transaksi yang baru dibuat
     * @throws Exception jika gagal menyimpan
     */
    int insert(Transaction transaction) throws Exception;

    /**
     * Mencari transaksi berdasarkan ID
     * @param id ID transaksi
     * @return Transaction lengkap dengan items
     * @throws Exception jika gagal mencari
     */
    Transaction findById(int id) throws Exception;

    /**
     * Mencari transaksi berdasarkan kode transaksi
     * @param code kode transaksi
     * @return Transaction lengkap dengan items
     * @throws Exception jika gagal mencari
     */
    Transaction findByCode(String code) throws Exception;

    /**
     * Mendapatkan semua transaksi
     * @return List semua transaksi
     * @throws Exception jika gagal membaca
     */
    List<Transaction> findAll() throws Exception;

    /**
     * Mendapatkan transaksi berdasarkan tanggal
     * @param date tanggal transaksi
     * @return List transaksi pada tanggal tersebut
     * @throws Exception jika gagal mencari
     */
    List<Transaction> findByDate(LocalDate date) throws Exception;

    /**
     * Mendapatkan transaksi dalam rentang tanggal
     * @param startDate tanggal awal
     * @param endDate tanggal akhir
     * @return List transaksi dalam rentang
     * @throws Exception jika gagal mencari
     */
    List<Transaction> findByDateRange(LocalDate startDate, LocalDate endDate) throws Exception;

    /**
     * Mendapatkan total penjualan harian
     * @param date tanggal
     * @return total penjualan
     * @throws Exception jika gagal menghitung
     */
    double getDailySalesTotal(LocalDate date) throws Exception;

    /**
     * Mendapatkan jumlah transaksi harian
     * @param date tanggal
     * @return jumlah transaksi
     * @throws Exception jika gagal menghitung
     */
    int getDailyTransactionCount(LocalDate date) throws Exception;
}
