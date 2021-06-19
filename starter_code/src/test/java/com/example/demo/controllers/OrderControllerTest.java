package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyString;

public class OrderControllerTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderController orderController;

    @Before
    public void setup(){
        this.orderController = new OrderController();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void verify_submit_order_happy_path(){
        Mockito.when(userRepository.findByUsername(anyString())).thenReturn(createUser());

        ResponseEntity<UserOrder> response = orderController.submit(createUser().getUsername());

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(new BigDecimal("25.0"), response.getBody().getTotal());
    }

    /*
        Negative Test: tests how OrderController.submit response provided user that not exists
     */
    @Test
    public void verify_submit_order_unhappy_path(){
        Mockito.when(userRepository.findByUsername(anyString())).thenReturn(null);

        ResponseEntity response = orderController.submit(createUser().getUsername());

        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void verify_get_orders_for_user_happy_path(){
        Mockito.when(userRepository.findByUsername(anyString())).thenReturn(createUser());
        Mockito.when(orderRepository.findByUser(createUser())).thenReturn(new ArrayList<UserOrder>());

        ResponseEntity response = orderController.getOrdersForUser(createUser().getUsername());

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /*
        Negative Test: tests how OrderController.getOrdersForUser response provided user that not exists
     */
    @Test
    public void verify_get_orders_for_user_unhappy_path(){
        Mockito.when(userRepository.findByUsername(anyString())).thenReturn(null);
        Mockito.when(orderRepository.findByUser(createUser())).thenReturn(new ArrayList<UserOrder>());

        ResponseEntity response = orderController.getOrdersForUser(createUser().getUsername());

        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    //Helper Methods
    private User createUser() {
        User user = new User();
        Cart cart = new Cart();

        cart.addItem(createItem());
        cart.addItem(createItem());

        user.setId(1l);
        user.setUsername("SomeUserName");
        user.setCart(cart);

        return user;
    }

    private Item createItem() {
        Item item = new Item();

        item.setId(1l);
        item.setName("SomeItemName");
        item.setPrice(new BigDecimal(12.5));

        return item;
    }
}
