package com.quickkart.config;

import com.quickkart.entity.Product;
import com.quickkart.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
@Order(2)
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        if (productRepository.count() == 0) {
            initializeProducts();
        }
    }

    private void initializeProducts() {
        // Electronics
        productRepository.save(new Product("iPhone 15 Pro", "Latest Apple smartphone with advanced features", 
            new BigDecimal("999.99"), 50, "Electronics"));
        productRepository.save(new Product("Samsung Galaxy S24", "Flagship Android smartphone", 
            new BigDecimal("899.99"), 30, "Electronics"));
        productRepository.save(new Product("MacBook Air M2", "Lightweight laptop with M2 chip", 
            new BigDecimal("1199.99"), 25, "Electronics"));
        productRepository.save(new Product("Dell XPS 13", "Premium ultrabook for professionals", 
            new BigDecimal("1099.99"), 20, "Electronics"));
        productRepository.save(new Product("iPad Pro", "Professional tablet for creative work", 
            new BigDecimal("799.99"), 40, "Electronics"));

        // Clothing
        productRepository.save(new Product("Nike Air Max 270", "Comfortable running shoes", 
            new BigDecimal("129.99"), 100, "Clothing"));
        productRepository.save(new Product("Levi's 501 Jeans", "Classic straight-fit jeans", 
            new BigDecimal("79.99"), 75, "Clothing"));
        productRepository.save(new Product("Adidas Hoodie", "Comfortable cotton hoodie", 
            new BigDecimal("59.99"), 60, "Clothing"));
        productRepository.save(new Product("Ray-Ban Sunglasses", "Classic aviator sunglasses", 
            new BigDecimal("149.99"), 35, "Clothing"));
        productRepository.save(new Product("North Face Jacket", "Waterproof outdoor jacket", 
            new BigDecimal("199.99"), 25, "Clothing"));

        // Home & Garden
        productRepository.save(new Product("Dyson V15 Vacuum", "Powerful cordless vacuum cleaner", 
            new BigDecimal("449.99"), 15, "Home & Garden"));
        productRepository.save(new Product("Instant Pot Duo", "Multi-use pressure cooker", 
            new BigDecimal("89.99"), 45, "Home & Garden"));
        productRepository.save(new Product("Philips Air Fryer", "Healthy cooking appliance", 
            new BigDecimal("129.99"), 30, "Home & Garden"));
        productRepository.save(new Product("Robot Vacuum", "Automated floor cleaning robot", 
            new BigDecimal("299.99"), 20, "Home & Garden"));
        productRepository.save(new Product("Smart Thermostat", "WiFi-enabled temperature control", 
            new BigDecimal("179.99"), 40, "Home & Garden"));

        // Books
        productRepository.save(new Product("The Great Gatsby", "Classic American novel", 
            new BigDecimal("12.99"), 200, "Books"));
        productRepository.save(new Product("Clean Code", "Programming best practices guide", 
            new BigDecimal("39.99"), 150, "Books"));
        productRepository.save(new Product("Atomic Habits", "Self-improvement bestseller", 
            new BigDecimal("16.99"), 180, "Books"));
        productRepository.save(new Product("Dune", "Science fiction epic novel", 
            new BigDecimal("14.99"), 120, "Books"));
        productRepository.save(new Product("The Psychology of Money", "Financial wisdom book", 
            new BigDecimal("18.99"), 90, "Books"));

        // Sports
        productRepository.save(new Product("Yoga Mat", "Non-slip exercise mat", 
            new BigDecimal("29.99"), 80, "Sports"));
        productRepository.save(new Product("Dumbbells Set", "Adjustable weight training set", 
            new BigDecimal("199.99"), 25, "Sports"));
        productRepository.save(new Product("Tennis Racket", "Professional grade tennis racket", 
            new BigDecimal("149.99"), 35, "Sports"));
        productRepository.save(new Product("Basketball", "Official size basketball", 
            new BigDecimal("24.99"), 60, "Sports"));
        productRepository.save(new Product("Fitness Tracker", "Heart rate and activity monitor", 
            new BigDecimal("99.99"), 70, "Sports"));
    }
}
