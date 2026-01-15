package com.upb.agripos.dao.interfaces;
import com.upb.agripos.model.Transaction;
import com.upb.agripos.model.TransactionItem;
import java.sql.SQLException;
import java.util.List;

public interface TransactionDAO {
    void saveTransaction(Transaction transaction) throws SQLException;
    Transaction getTransactionById(int id);
    List<Transaction> getAllTransactions();
    void updateTransaction(Transaction transaction);
    void deleteTransaction(int id);
    List<TransactionItem> getTransactionItems(int transactionId);
    List<Transaction> getTransactionsByStatus(String status);
}