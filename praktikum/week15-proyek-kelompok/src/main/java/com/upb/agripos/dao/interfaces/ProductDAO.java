package com.upb.agripos.dao.interfaces;
import com.upb.agripos.model.Product;
import java.util.List;

public interface ProductDAO {
    void save(Product product);
    void update(Product product);
    void delete(String kode);
    Product findByKode(String kode);
    List<Product> findAll();
}