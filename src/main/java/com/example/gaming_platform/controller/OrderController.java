package com.example.gaming_platform.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gaming_platform.entity.Orders;
import com.example.gaming_platform.repository.OrdersRepository;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrdersRepository ordersRepository;

    public OrderController(OrdersRepository ordersRepository){
        this.ordersRepository = ordersRepository;
    }

    // GET /api/orders/
    @GetMapping
    public Iterable<Orders> getAllOrders(){
        return ordersRepository.findAll();
    }

    //GET /api/orders/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Orders> getOrderById(@PathVariable Long id){
        return ordersRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //POST /api/orders
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Orders> createOrder(@RequestBody Orders order){
        Orders saved = ordersRepository.save(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
