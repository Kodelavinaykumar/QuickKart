package com.quickkart.service;

import com.quickkart.entity.Order;
import com.quickkart.entity.OrderItem;
import com.quickkart.entity.Product;
import com.quickkart.entity.User;
import com.quickkart.repository.OrderRepository;
import com.quickkart.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductService productService;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserIdOrderByOrderDateDesc(userId);
    }

    @Transactional
    public Order createOrder(User user, List<OrderItem> orderItems, String shippingAddress) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        
        // Calculate total amount and validate stock
        for (OrderItem item : orderItems) {
            Optional<Product> productOpt = productService.getProductById(item.getProduct().getId());
            if (productOpt.isEmpty()) {
                throw new RuntimeException("Product not found: " + item.getProduct().getId());
            }
            
            Product product = productOpt.get();
            if (product.getStockQuantity() < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }
            
            item.setPrice(product.getPrice());
            totalAmount = totalAmount.add(item.getTotalPrice());
        }

        // Create order
        Order order = new Order(user, totalAmount, shippingAddress);
        order = orderRepository.save(order);

        // Create order items and update stock
        for (OrderItem item : orderItems) {
            item.setOrder(order);
            orderItemRepository.save(item);
            productService.updateStock(item.getProduct().getId(), item.getQuantity());
        }

        return order;
    }

    public Order updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus(status);
            return orderRepository.save(order);
        }
        throw new RuntimeException("Order not found: " + orderId);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public List<Order> getOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatus(status);
    }
}
