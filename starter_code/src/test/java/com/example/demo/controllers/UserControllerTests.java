package com.example.demo.controllers;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class UserControllerTests {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserRepository userRepo;

    @Mock
    private CartRepository cartRepo;

    @Mock
    private BCryptPasswordEncoder encoder;

    @Before
    public void setup() {
        this.userController = new UserController();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void create_user_happy_path() {
        when(encoder.encode(anyString())).thenReturn("Test HashedPassword");
        when(userRepo.findByUsername(anyString())).thenReturn(null);

        CreateUserRequest requestBody = new CreateUserRequest("Test Username", "Test Password", "Test Password");
        ResponseEntity<User> response = userController.createUser(requestBody);
        User responseUser = response.getBody();

        // Assertions Part 1
        Assert.assertNotNull(response);
        Assert.assertNotNull(responseUser);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals("Test Username", response.getBody().getUsername());
        Assert.assertEquals("Test HashedPassword", responseUser.getPassword());

        // Try to make password less than 8 characters
        requestBody.setPassword("123456");
        response = userController.createUser(requestBody);
        responseUser = response.getBody();

        // Assertions Part 2
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assert.assertNull(responseUser);
    }

    /**
        Negative Test: tests how UserController response provided existing username
     */
    @Test
    public void create_user_unhappy_path(){
        // Mimic existing username
        when(userRepo.findByUsername(anyString())).thenReturn(new User());
        when(encoder.encode(anyString())).thenReturn("Test HashedPassword");

        CreateUserRequest requestBody = new CreateUserRequest("Test Username", "Test Password", "Test Password");
        ResponseEntity<User> response = userController.createUser(requestBody);
        User responseUser = response.getBody();

        // Assertions
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assert.assertNull(responseUser);
        Assert.assertEquals("Username Already Exist", response.getHeaders().get("Error").get(0));
    }
}
