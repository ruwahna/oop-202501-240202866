package com.upb.agripos.service;

import com.upb.agripos.dao.ProductDAO;
import com.upb.agripos.exception.DataNotFoundException;
import com.upb.agripos.exception.ValidationException;
import com.upb.agripos.model.Product;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit Test untuk ProductService
 * Menggunakan Mockito untuk mock ProductDAO
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService Tests")
class ProductServiceTest {

    @Mock
    private ProductDAO productDAO;

    @InjectMocks
    private ProductService productService;

    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        sampleProduct = new Product("P001", "Beras Premium", "Beras", 65000.0, 100);
    }

    @Nested
    @DisplayName("getAllProducts Tests")
    class GetAllProductsTests {

        @Test
        @DisplayName("Harus mengembalikan semua produk")
        void shouldReturnAllProducts() {
            // Arrange
            List<Product> expectedProducts = Arrays.asList(
                sampleProduct,
                new Product("P002", "Gula Pasir", "Bumbu", 14000.0, 50)
            );
            when(productDAO.findAll()).thenReturn(expectedProducts);

            // Act
            List<Product> result = productService.getAllProducts();

            // Assert
            assertEquals(2, result.size());
            verify(productDAO, times(1)).findAll();
        }

        @Test
        @DisplayName("Harus mengembalikan list kosong jika tidak ada produk")
        void shouldReturnEmptyListWhenNoProducts() {
            // Arrange
            when(productDAO.findAll()).thenReturn(List.of());

            // Act
            List<Product> result = productService.getAllProducts();

            // Assert
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("getProductByCode Tests")
    class GetProductByCodeTests {

        @Test
        @DisplayName("Harus mengembalikan produk jika ditemukan")
        void shouldReturnProductWhenFound() throws DataNotFoundException {
            // Arrange
            when(productDAO.findByCode("P001")).thenReturn(Optional.of(sampleProduct));

            // Act
            Product result = productService.getProductByCode("P001");

            // Assert
            assertNotNull(result);
            assertEquals("P001", result.getCode());
            assertEquals("Beras Premium", result.getName());
        }

        @Test
        @DisplayName("Harus throw DataNotFoundException jika tidak ditemukan")
        void shouldThrowExceptionWhenNotFound() {
            // Arrange
            when(productDAO.findByCode("XXX")).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(DataNotFoundException.class, () -> 
                productService.getProductByCode("XXX")
            );
        }
    }

    @Nested
    @DisplayName("addProduct Tests")
    class AddProductTests {

        @Test
        @DisplayName("Harus berhasil menambah produk valid")
        void shouldAddValidProduct() throws ValidationException {
            // Arrange
            Product newProduct = new Product("P003", "Minyak Goreng", "Minyak", 28000.0, 30);
            when(productDAO.save(any(Product.class))).thenReturn(true);

            // Act
            productService.addProduct(newProduct);

            // Assert
            verify(productDAO, times(1)).save(newProduct);
        }

        @Test
        @DisplayName("Harus throw ValidationException jika kode kosong")
        void shouldThrowExceptionWhenCodeEmpty() {
            // Arrange
            Product invalidProduct = new Product("", "Test", "Category", 10000.0, 10);

            // Act & Assert
            assertThrows(ValidationException.class, () -> 
                productService.addProduct(invalidProduct)
            );
            verify(productDAO, never()).save(any());
        }

        @Test
        @DisplayName("Harus throw ValidationException jika nama kosong")
        void shouldThrowExceptionWhenNameEmpty() {
            // Arrange
            Product invalidProduct = new Product("P001", "", "Category", 10000.0, 10);

            // Act & Assert
            assertThrows(ValidationException.class, () -> 
                productService.addProduct(invalidProduct)
            );
        }

        @Test
        @DisplayName("Harus throw ValidationException jika harga negatif")
        void shouldThrowExceptionWhenPriceNegative() {
            // Arrange
            Product invalidProduct = new Product("P001", "Test", "Category", -5000.0, 10);

            // Act & Assert
            assertThrows(ValidationException.class, () -> 
                productService.addProduct(invalidProduct)
            );
        }

        @Test
        @DisplayName("Harus throw ValidationException jika stok negatif")
        void shouldThrowExceptionWhenStockNegative() {
            // Arrange
            Product invalidProduct = new Product("P001", "Test", "Category", 10000.0, -5);

            // Act & Assert
            assertThrows(ValidationException.class, () -> 
                productService.addProduct(invalidProduct)
            );
        }
    }

    @Nested
    @DisplayName("updateProduct Tests")
    class UpdateProductTests {

        @Test
        @DisplayName("Harus berhasil update produk")
        void shouldUpdateProduct() throws ValidationException {
            // Arrange
            when(productDAO.update(any(Product.class))).thenReturn(true);

            // Act
            productService.updateProduct(sampleProduct);

            // Assert
            verify(productDAO, times(1)).update(sampleProduct);
        }

        @Test
        @DisplayName("Harus throw ValidationException jika produk invalid")
        void shouldThrowExceptionForInvalidProduct() {
            // Arrange
            Product invalidProduct = new Product(null, "Test", "Category", 10000.0, 10);

            // Act & Assert
            assertThrows(ValidationException.class, () -> 
                productService.updateProduct(invalidProduct)
            );
        }
    }

    @Nested
    @DisplayName("deleteProduct Tests")
    class DeleteProductTests {

        @Test
        @DisplayName("Harus berhasil hapus produk")
        void shouldDeleteProduct() {
            // Arrange
            when(productDAO.delete("P001")).thenReturn(true);

            // Act
            productService.deleteProduct("P001");

            // Assert
            verify(productDAO, times(1)).delete("P001");
        }
    }

    @Nested
    @DisplayName("getProductsByCategory Tests")
    class GetProductsByCategoryTests {

        @Test
        @DisplayName("Harus mengembalikan produk sesuai kategori")
        void shouldReturnProductsByCategory() {
            // Arrange
            List<Product> expectedProducts = List.of(sampleProduct);
            when(productDAO.findByCategory("Beras")).thenReturn(expectedProducts);

            // Act
            List<Product> result = productService.getProductsByCategory("Beras");

            // Assert
            assertEquals(1, result.size());
            assertEquals("Beras", result.get(0).getCategory());
        }
    }

    @Nested
    @DisplayName("searchProducts Tests")
    class SearchProductsTests {

        @Test
        @DisplayName("Harus mencari produk berdasarkan keyword")
        void shouldSearchProducts() {
            // Arrange
            List<Product> expectedProducts = List.of(sampleProduct);
            when(productDAO.search("beras")).thenReturn(expectedProducts);

            // Act
            List<Product> result = productService.searchProducts("beras");

            // Assert
            assertEquals(1, result.size());
            verify(productDAO, times(1)).search("beras");
        }
    }
}
