package com.example.gaming_platform.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gaming_platform.entity.Orders;
import com.example.gaming_platform.repository.OrdersRepository;

/**
 * REST controller for order operations.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrdersRepository ordersRepository;

/**
 * Creates a new OrderController instance.
 *
 * @param ordersRepository the orders repository
 */
    public OrderController(OrdersRepository ordersRepository){
        this.ordersRepository = ordersRepository;
    }

    // GET /api/orders/
/**
 * Retrieves all orders.
 *
 * @return all orders
 */
    @GetMapping
    public Iterable<Orders> getAllOrders(){
        return ordersRepository.findAll();
    }

    //GET /api/orders/{id}
/**
 * Retrieves a order by ID.
 *
 * @param id the ID
 * @return the matching order when found
 */
    @GetMapping("/{id}")
    public ResponseEntity<Orders> getOrderById(@PathVariable Long id){
        return ordersRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //POST /api/orders
/**
 * Creates a new order.
 *
 * @param order the order
 * @return the created order
 */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Orders> createOrder(@RequestBody Orders order){
        Orders saved = ordersRepository.save(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
