package com.upb.agripos.payment;

import com.upb.agripos.exception.PaymentException;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Test untuk Payment Strategy Pattern
 */
@DisplayName("Payment Strategy Tests")
class PaymentMethodTest {

    @Nested
    @DisplayName("CashPayment Tests")
    class CashPaymentTests {

        private CashPayment cashPayment;

        @BeforeEach
        void setUp() {
            cashPayment = new CashPayment();
        }

        @Test
        @DisplayName("Harus mengembalikan nama metode yang benar")
        void shouldReturnCorrectMethodName() {
            assertEquals("CASH", cashPayment.getMethodName());
        }

        @Test
        @DisplayName("Harus berhasil memproses pembayaran tunai yang cukup")
        void shouldProcessSufficientPayment() throws PaymentException {
            // Act
            double change = cashPayment.processPayment(50000, 100000);

            // Assert
            assertEquals(50000, change, 0.01);
        }

        @Test
        @DisplayName("Harus throw PaymentException jika pembayaran tidak cukup")
        void shouldThrowExceptionWhenPaymentInsufficient() {
            // Act & Assert
            assertThrows(PaymentException.class, () -> 
                cashPayment.processPayment(100000, 50000)
            );
        }

        @Test
        @DisplayName("Validasi harus mengembalikan true jika jumlah cukup")
        void shouldValidateSuccessfully() {
            // Act
            boolean result = cashPayment.validatePayment(50000, 50000);

            // Assert
            assertTrue(result);
        }

        @Test
        @DisplayName("Validasi harus mengembalikan false jika jumlah tidak cukup")
        void shouldFailValidationWhenInsufficient() {
            // Act
            boolean result = cashPayment.validatePayment(100000, 50000);

            // Assert
            assertFalse(result);
        }

        @Test
        @DisplayName("Harus mengembalikan deskripsi struk yang benar")
        void shouldReturnCorrectReceiptDescription() {
            // Act
            String description = cashPayment.getReceiptDescription(50000, 100000);

            // Assert
            assertTrue(description.contains("Tunai"));
            assertTrue(description.contains("100000"));
            assertTrue(description.contains("50000"));
        }
    }

    @Nested
    @DisplayName("EWalletPayment Tests")
    class EWalletPaymentTests {

        private EWalletPayment eWalletPayment;

        @BeforeEach
        void setUp() {
            eWalletPayment = new EWalletPayment();
        }

        @Test
        @DisplayName("Harus mengembalikan nama metode yang benar")
        void shouldReturnCorrectMethodName() {
            assertEquals("E-WALLET", eWalletPayment.getMethodName());
        }

        @Test
        @DisplayName("Harus berhasil memproses pembayaran e-wallet")
        void shouldProcessEWalletPayment() throws PaymentException {
            // Act
            double change = eWalletPayment.processPayment(50000, 50000);

            // Assert
            assertEquals(0, change, 0.01);
        }

        @Test
        @DisplayName("Harus mengembalikan kembalian jika pembayaran lebih")
        void shouldReturnChangeWhenOverpaid() throws PaymentException {
            // Act
            double change = eWalletPayment.processPayment(50000, 75000);

            // Assert
            assertEquals(25000, change, 0.01);
        }

        @Test
        @DisplayName("Validasi harus mengembalikan true")
        void shouldValidateSuccessfully() {
            // Act
            boolean result = eWalletPayment.validatePayment(50000, 50000);

            // Assert
            assertTrue(result);
        }
    }

    @Nested
    @DisplayName("PaymentMethodFactory Tests")
    class PaymentMethodFactoryTests {

        @BeforeEach
        void setUp() {
            // Register default payment methods
            PaymentMethodFactory.registerPaymentMethod("CASH", new CashPayment());
            PaymentMethodFactory.registerPaymentMethod("E-WALLET", new EWalletPayment());
        }

        @Test
        @DisplayName("Harus mengembalikan CashPayment untuk CASH")
        void shouldReturnCashPaymentForCash() {
            // Act
            PaymentMethod method = PaymentMethodFactory.getPaymentMethod("CASH");

            // Assert
            assertNotNull(method);
            assertTrue(method instanceof CashPayment);
        }

        @Test
        @DisplayName("Harus mengembalikan EWalletPayment untuk E-WALLET")
        void shouldReturnEWalletPaymentForEWallet() {
            // Act
            PaymentMethod method = PaymentMethodFactory.getPaymentMethod("E-WALLET");

            // Assert
            assertNotNull(method);
            assertTrue(method instanceof EWalletPayment);
        }

        @Test
        @DisplayName("Harus mengembalikan null untuk metode tidak dikenal")
        void shouldReturnNullForUnknownMethod() {
            // Act
            PaymentMethod method = PaymentMethodFactory.getPaymentMethod("UNKNOWN");

            // Assert
            assertNull(method);
        }

        @Test
        @DisplayName("Harus mengembalikan semua metode yang tersedia")
        void shouldReturnAllAvailableMethods() {
            // Act
            var methods = PaymentMethodFactory.getAvailablePaymentMethods();

            // Assert
            assertTrue(methods.contains("CASH"));
            assertTrue(methods.contains("E-WALLET"));
        }

        @Test
        @DisplayName("Harus bisa mendaftarkan metode baru")
        void shouldRegisterNewMethod() {
            // Arrange
            PaymentMethod customMethod = new PaymentMethod() {
                @Override
                public String getMethodName() { return "CUSTOM"; }
                @Override
                public double processPayment(double total, double amountPaid) { return amountPaid - total; }
                @Override
                public boolean validatePayment(double total, double amountPaid) { return amountPaid >= total; }
                @Override
                public String getReceiptDescription(double total, double amountPaid) { return "Custom payment"; }
            };

            // Act
            PaymentMethodFactory.registerPaymentMethod("CUSTOM", customMethod);
            PaymentMethod retrieved = PaymentMethodFactory.getPaymentMethod("CUSTOM");

            // Assert
            assertNotNull(retrieved);
            assertEquals("CUSTOM", retrieved.getMethodName());
        }
    }
}
