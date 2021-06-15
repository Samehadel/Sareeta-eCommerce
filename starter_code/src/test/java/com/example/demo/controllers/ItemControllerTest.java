package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    @InjectMocks
    private ItemController itemController;

    @Mock
    private ItemRepository itemRepo;

    @Before
    public void setup() {
        this.itemController = new ItemController();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void verify_get_items(){
        ResponseEntity response = itemController.getItems();

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void  get_item_by_id_happy_path(){
        when(itemRepo.findById(anyLong())).thenReturn(Optional.of(new Item()));

        ResponseEntity response = itemController.getItemById(1l);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void  get_item_by_id_unhappy_path(){
        when(itemRepo.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity response = itemController.getItemById(1l);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void  get_item_by_name_happy_path(){
        when(itemRepo.findByName(anyString())).thenReturn(Arrays.asList(new Item()));

        ResponseEntity response = itemController.getItemsByName("Item Name");
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void  get_item_by_name_unhappy_path(){
        when(itemRepo.findByName(anyString())).thenReturn(null);

        ResponseEntity response = itemController.getItemsByName("Item Name");
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
