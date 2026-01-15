package com.upb.agripos.dao.interfaces;

import com.upb.agripos.model.Payment;
import java.util.List;

public interface PaymentDAO {
    void save(Payment payment);
    Payment getByTransactionId(int transactionId);
    List<Payment> getAllPayments();
}