package com.upb.agripos.dao.ipml;

import com.upb.agripos.dao.DatabaseConnection;
import com.upb.agripos.dao.interfaces.PaymentDAO;
import com.upb.agripos.model.Payment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAOImpl implements PaymentDAO {

    @Override
    public void save(Payment p) {
        String sql = "INSERT INTO payments (transaction_id, metode, jumlah_bayar, kembalian) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, p.getTransactionId());
            ps.setString(2, p.getMetode()); // Diambil dari Strategy (Cash/E-Wallet)
            ps.setDouble(3, p.getJumlahBayar());
            ps.setDouble(4, p.getKembalian());
            
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving payment: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public Payment getByTransactionId(int transactionId) {
        String sql = "SELECT * FROM payments WHERE transaction_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, transactionId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToPayment(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving payment: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Payment> getAllPayments() {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT * FROM payments";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                list.add(mapResultSetToPayment(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all payments: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public void update(Payment p) {
        String sql = "UPDATE payments SET metode = ?, jumlah_bayar = ?, kembalian = ? WHERE transaction_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, p.getMetode());
            ps.setDouble(2, p.getJumlahBayar());
            ps.setDouble(3, p.getKembalian());
            ps.setInt(4, p.getTransactionId());
            
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating payment: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void delete(int transactionId) {
        String sql = "DELETE FROM payments WHERE transaction_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, transactionId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting payment: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Payment mapResultSetToPayment(ResultSet rs) throws SQLException {
        Payment p = new Payment();
        p.setTransactionId(rs.getInt("transaction_id"));
        p.setMetode(rs.getString("metode"));
        p.setJumlahBayar(rs.getDouble("jumlah_bayar"));
        p.setKembalian(rs.getDouble("kembalian"));
        return p;
    }
}