package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;

public class CartControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private CartController cartController;

    @Before
    public void setup() {
        this.cartController = new CartController();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void verify_add_to_cart_happy_path() {
        Mockito.when(userRepository.findByUsername(anyString())).thenReturn(createUser());
        Mockito.when(itemRepository.findById(anyLong())).thenReturn(Optional.of(createItem()));

        ModifyCartRequest requestBody = new ModifyCartRequest(createUser().getUsername(), createItem().getId(), 5);
        ResponseEntity response = cartController.addTocart(requestBody);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /*
        Negative Test: tests how CartController.addToCart response provided username that not exists
     */
    @Test
    public void verify_add_to_cart_unhappy_path() {
        Mockito.when(userRepository.findByUsername(anyString())).thenReturn(null);
        Mockito.when(itemRepository.findById(anyLong())).thenReturn(Optional.of(createItem()));

        ModifyCartRequest requestBody = new ModifyCartRequest(anyString(), createItem().getId(), 5);
        ResponseEntity response = cartController.addTocart(requestBody);

        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /*
       Negative Test: tests how CartController.addToCart response provided item that not exists
     */
    @Test
    public void verify_add_to_cart_unhappy_path2() {
        Mockito.when(userRepository.findByUsername(anyString())).thenReturn(createUser());
        Mockito.when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        ModifyCartRequest requestBody = new ModifyCartRequest(createUser().getUsername(), createItem().getId(), 5);
        ResponseEntity response = cartController.addTocart(requestBody);

        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void verify_remove_from_cart_happy_path(){
        Mockito.when(userRepository.findByUsername(anyString())).thenReturn(createUser());
        Mockito.when(itemRepository.findById(anyLong())).thenReturn(Optional.of(createItem()));

        ModifyCartRequest requestBody = new ModifyCartRequest(createUser().getUsername(), createItem().getId(), 5);
        ResponseEntity response = cartController.removeFromCart(requestBody);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /*
        Negative Test: tests how CartController.removeFromCart response provided username that not exists
     */
    @Test
    public void verify_remove_from_cart_unhappy_path(){
        Mockito.when(userRepository.findByUsername(anyString())).thenReturn(null);
        Mockito.when(itemRepository.findById(anyLong())).thenReturn(Optional.of(createItem()));

        ModifyCartRequest requestBody = new ModifyCartRequest(anyString(), createItem().getId(), 5);
        ResponseEntity response = cartController.removeFromCart(requestBody);

        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    /*
        Negative Test: tests how CartController.removeFromCart response provided item that not exists
    */
    @Test
    public void verify_remove_from_cart_unhappy_path2(){
        Mockito.when(userRepository.findByUsername(anyString())).thenReturn(createUser());
        Mockito.when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        ModifyCartRequest requestBody = new ModifyCartRequest(createUser().getUsername(), anyLong(), 5);
        ResponseEntity response = cartController.removeFromCart(requestBody);

        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private User createUser() {
        User user = new User();

        user.setId(1l);
        user.setUsername("SomeUserName");
        user.setCart(new Cart());

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
