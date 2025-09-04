package com.quickkart.controller;

import com.quickkart.entity.Product;
import com.quickkart.entity.Order;
import com.quickkart.entity.User;
import com.quickkart.service.ProductService;
import com.quickkart.service.OrderService;
import com.quickkart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    // Admin login endpoint
    @PostMapping("/login")
    public ResponseEntity<?> adminLogin(@RequestBody AdminLoginRequest request) {
        Optional<User> user = userService.getUserByUsername(request.getUsername());
        if (user.isPresent() && 
            user.get().getPassword().equals(request.getPassword()) && 
            user.get().getRole() == User.Role.ADMIN) {
            return ResponseEntity.ok(user.get());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid admin credentials");
    }

    // Product management
    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        Product savedProduct = productService.saveProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody Product product) {
        Optional<Product> existingProduct = productService.getProductById(id);
        if (existingProduct.isPresent()) {
            product.setId(id);
            Product updatedProduct = productService.saveProduct(product);
            return ResponseEntity.ok(updatedProduct);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent()) {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Order management
    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/orders/{id}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id, @RequestBody StatusUpdateRequest request) {
        try {
            Order updatedOrder = orderService.updateOrderStatus(id, request.getStatus());
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // User management
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<?> updateUserRole(@PathVariable Long id, @RequestBody RoleUpdateRequest request) {
        Optional<User> userOpt = userService.getUserById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setRole(request.getRole());
            User updatedUser = userService.saveUser(user);
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.notFound().build();
    }

    // Dashboard stats
    @GetMapping("/stats")
    public ResponseEntity<AdminStats> getAdminStats() {
        long totalProducts = productService.getAllProducts().size();
        long totalUsers = userService.getAllUsers().size();
        long totalOrders = orderService.getAllOrders().size();
        long pendingOrders = orderService.getOrdersByStatus(Order.OrderStatus.PENDING).size();

        AdminStats stats = new AdminStats(totalProducts, totalUsers, totalOrders, pendingOrders);
        return ResponseEntity.ok(stats);
    }

    // Inner classes for request bodies
    public static class AdminLoginRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class StatusUpdateRequest {
        private Order.OrderStatus status;

        public Order.OrderStatus getStatus() { return status; }
        public void setStatus(Order.OrderStatus status) { this.status = status; }
    }

    public static class RoleUpdateRequest {
        private User.Role role;

        public User.Role getRole() { return role; }
        public void setRole(User.Role role) { this.role = role; }
    }

    public static class AdminStats {
        private long totalProducts;
        private long totalUsers;
        private long totalOrders;
        private long pendingOrders;

        public AdminStats(long totalProducts, long totalUsers, long totalOrders, long pendingOrders) {
            this.totalProducts = totalProducts;
            this.totalUsers = totalUsers;
            this.totalOrders = totalOrders;
            this.pendingOrders = pendingOrders;
        }

        public long getTotalProducts() { return totalProducts; }
        public long getTotalUsers() { return totalUsers; }
        public long getTotalOrders() { return totalOrders; }
        public long getPendingOrders() { return pendingOrders; }
    }
}
