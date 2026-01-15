package com.upb.agripos.service;

import com.upb.agripos.dao.DatabaseConnection;
import com.upb.agripos.exception.ValidationException;
import java.sql.*;
import java.time.LocalDate;

public class ReportService {

    /**
     * Ambil total revenue untuk hari tertentu
     */
    public double getDailyRevenue(LocalDate date) throws ValidationException {
        if (date == null) {
            throw new ValidationException("Date", "Tanggal tidak boleh null");
        }
        
        String sql = "SELECT SUM(total) FROM transactions WHERE DATE(tanggal) = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setDate(1, Date.valueOf(date));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double revenue = rs.getDouble(1);
                return revenue > 0 ? revenue : 0.0;
            }
        } catch (SQLException e) {
            System.err.println("Error getting daily revenue: " + e.getMessage());
            e.printStackTrace();
        }
        return 0.0;
    }

    /**
     * Ambil total revenue untuk range tanggal
     */
    public double getRangeRevenue(LocalDate startDate, LocalDate endDate) throws ValidationException {
        if (startDate == null || endDate == null) {
            throw new ValidationException("Date Range", "Tanggal awal dan akhir tidak boleh null");
        }
        if (startDate.isAfter(endDate)) {
            throw new ValidationException("Date Range", "Tanggal awal harus sebelum tanggal akhir");
        }
        
        String sql = "SELECT SUM(total) FROM transactions WHERE DATE(tanggal) BETWEEN ? AND ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setDate(1, Date.valueOf(startDate));
            ps.setDate(2, Date.valueOf(endDate));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double revenue = rs.getDouble(1);
                return revenue > 0 ? revenue : 0.0;
            }
        } catch (SQLException e) {
            System.err.println("Error getting range revenue: " + e.getMessage());
            e.printStackTrace();
        }
        return 0.0;
    }

    /**
     * Ambil jumlah transaksi untuk hari tertentu
     */
    public int getTotalTransactions(LocalDate date) throws ValidationException {
        if (date == null) {
            throw new ValidationException("Date", "Tanggal tidak boleh null");
        }
        
        String sql = "SELECT COUNT(*) FROM transactions WHERE DATE(tanggal) = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setDate(1, Date.valueOf(date));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting total transactions: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Ambil jumlah transaksi untuk range tanggal
     */
    public int getTotalTransactionsRange(LocalDate startDate, LocalDate endDate) throws ValidationException {
        if (startDate == null || endDate == null) {
            throw new ValidationException("Date Range", "Tanggal awal dan akhir tidak boleh null");
        }
        if (startDate.isAfter(endDate)) {
            throw new ValidationException("Date Range", "Tanggal awal harus sebelum tanggal akhir");
        }
        
        String sql = "SELECT COUNT(*) FROM transactions WHERE DATE(tanggal) BETWEEN ? AND ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setDate(1, Date.valueOf(startDate));
            ps.setDate(2, Date.valueOf(endDate));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting range transactions: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Ambil average transaction untuk hari tertentu
     */
    public double getAverageTransaction(LocalDate date) throws ValidationException {
        if (date == null) {
            throw new ValidationException("Date", "Tanggal tidak boleh null");
        }
        
        int count = getTotalTransactions(date);
        if (count == 0) return 0.0;
        
        double revenue = getDailyRevenue(date);
        return revenue / count;
    }
}