package com.quickkart.repository;

import com.quickkart.entity.Productimages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductimagesRepository extends JpaRepository<Productimages, Long> {

    // ✅ Find products by exact name
    List<Productimages> findByName(String name);

    // ✅ Find products where name contains a keyword (case-insensitive)
    List<Productimages> findByNameContainingIgnoreCase(String keyword);

    // ✅ Find products with price between two values
    List<Productimages> findByPriceBetween(Double minPrice, Double maxPrice);

    // ✅ Find products with stock greater than a given value
    List<Productimages> findByStockGreaterThan(Integer stock);

    // ✅ Find products with stock less than a given value
    List<Productimages> findByStockLessThan(Integer stock);
}
