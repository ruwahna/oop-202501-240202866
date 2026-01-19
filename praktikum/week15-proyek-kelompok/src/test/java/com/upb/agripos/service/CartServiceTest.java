package com.upb.agripos.service;

import com.upb.agripos.exception.OutOfStockException;
import com.upb.agripos.model.Cart;
import com.upb.agripos.model.CartItem;
import com.upb.agripos.model.Product;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Test untuk CartService
 * Testing tanpa mock karena CartService tidak memiliki dependency
 */
@DisplayName("CartService Tests")
class CartServiceTest {

    private CartService cartService;
    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        cartService = new CartService();
        sampleProduct = new Product("P001", "Beras Premium", "Beras", 65000.0, 100);
    }

    @Nested
    @DisplayName("addToCart Tests")
    class AddToCartTests {

        @Test
        @DisplayName("Harus berhasil menambah item ke keranjang")
        void shouldAddItemToCart() throws OutOfStockException {
            // Act
            cartService.addToCart(sampleProduct, 2);

            // Assert
            assertFalse(cartService.isCartEmpty());
            assertEquals(1, cartService.getCartItems().size());
        }

        @Test
        @DisplayName("Harus throw OutOfStockException jika stok tidak cukup")
        void shouldThrowExceptionWhenInsufficientStock() {
            // Act & Assert
            assertThrows(OutOfStockException.class, () -> 
                cartService.addToCart(sampleProduct, 150)
            );
        }

        @Test
        @DisplayName("Harus menambah quantity jika produk sudah ada di keranjang")
        void shouldIncreaseQuantityIfProductExists() throws OutOfStockException {
            // Arrange
            cartService.addToCart(sampleProduct, 2);

            // Act
            cartService.addToCart(sampleProduct, 3);

            // Assert
            List<CartItem> items = cartService.getCartItems();
            assertEquals(1, items.size());
            assertEquals(5, items.get(0).getQuantity());
        }
    }

    @Nested
    @DisplayName("removeFromCart Tests")
    class RemoveFromCartTests {

        @Test
        @DisplayName("Harus berhasil menghapus item dari keranjang")
        void shouldRemoveItemFromCart() throws OutOfStockException {
            // Arrange
            cartService.addToCart(sampleProduct, 2);

            // Act
            cartService.removeFromCart("P001");

            // Assert
            assertTrue(cartService.isCartEmpty());
        }

        @Test
        @DisplayName("Tidak error jika menghapus item yang tidak ada")
        void shouldNotErrorWhenRemovingNonExistentItem() {
            // Act & Assert (no exception)
            assertDoesNotThrow(() -> cartService.removeFromCart("XXX"));
        }
    }

    @Nested
    @DisplayName("updateQuantity Tests")
    class UpdateQuantityTests {

        @Test
        @DisplayName("Harus berhasil update quantity")
        void shouldUpdateQuantity() throws OutOfStockException {
            // Arrange
            cartService.addToCart(sampleProduct, 2);

            // Act
            cartService.updateQuantity("P001", 5);

            // Assert
            List<CartItem> items = cartService.getCartItems();
            assertEquals(5, items.get(0).getQuantity());
        }

        @Test
        @DisplayName("Harus throw OutOfStockException jika quantity melebihi stok")
        void shouldThrowExceptionWhenExceedingStock() throws OutOfStockException {
            // Arrange
            cartService.addToCart(sampleProduct, 2);

            // Act & Assert
            assertThrows(OutOfStockException.class, () -> 
                cartService.updateQuantity("P001", 150)
            );
        }
    }

    @Nested
    @DisplayName("calculateSubtotal Tests")
    class CalculateSubtotalTests {

        @Test
        @DisplayName("Harus menghitung subtotal dengan benar")
        void shouldCalculateSubtotalCorrectly() throws OutOfStockException {
            // Arrange
            cartService.addToCart(sampleProduct, 2);
            Product anotherProduct = new Product("P002", "Gula", "Bumbu", 14000.0, 50);
            cartService.addToCart(anotherProduct, 3);

            // Act
            double subtotal = cartService.calculateSubtotal();

            // Assert
            // 2 * 65000 + 3 * 14000 = 130000 + 42000 = 172000
            assertEquals(172000.0, subtotal, 0.01);
        }

        @Test
        @DisplayName("Subtotal harus 0 jika keranjang kosong")
        void shouldReturnZeroWhenCartEmpty() {
            // Act
            double subtotal = cartService.calculateSubtotal();

            // Assert
            assertEquals(0.0, subtotal, 0.01);
        }
    }

    @Nested
    @DisplayName("clearCart Tests")
    class ClearCartTests {

        @Test
        @DisplayName("Harus mengosongkan keranjang")
        void shouldClearCart() throws OutOfStockException {
            // Arrange
            cartService.addToCart(sampleProduct, 2);

            // Act
            cartService.clearCart();

            // Assert
            assertTrue(cartService.isCartEmpty());
        }
    }

    @Nested
    @DisplayName("getCart Tests")
    class GetCartTests {

        @Test
        @DisplayName("Harus mengembalikan Cart object")
        void shouldReturnCart() throws OutOfStockException {
            // Arrange
            cartService.addToCart(sampleProduct, 2);

            // Act
            Cart cart = cartService.getCart();

            // Assert
            assertNotNull(cart);
            assertEquals(1, cart.getItems().size());
        }
    }
}
