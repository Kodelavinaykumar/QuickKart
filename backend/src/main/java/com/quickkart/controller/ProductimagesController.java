package com.quickkart.controller;

import com.quickkart.entity.Product;
import com.quickkart.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/product-images")
@CrossOrigin(origins = "*")
public class ProductimagesController {

    @Autowired
    private ProductService productService;

    private final String uploadDir = "uploads/";

    // ✅ Update Product Image
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProductImage(@PathVariable Long id,
                                                      @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
        Optional<Product> existingProductOpt = productService.getProductById(id);
        if (existingProductOpt.isEmpty()) return ResponseEntity.notFound().build();

        Product existingProduct = existingProductOpt.get();

        if (image != null && !image.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            String filePath = uploadDir + fileName;
            File dest = new File(filePath);
            dest.getParentFile().mkdirs();
            image.transferTo(dest);
            existingProduct.setImageUrl("http://localhost:8084/" + filePath);
        }

        return ResponseEntity.ok(productService.saveProduct(existingProduct));
    }

    // ✅ Delete Product Image
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductImage(@PathVariable Long id) {
        Optional<Product> existingProduct = productService.getProductById(id);
        if (existingProduct.isEmpty()) return ResponseEntity.notFound().build();

        Product product = existingProduct.get();
        product.setImageUrl(null); // remove image reference
        productService.saveProduct(product);

        return ResponseEntity.ok("Product image removed successfully");
    }
}

