package com.redhat.productinventory.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.redhat.productinventory.dtos.InventoryDTO;
import com.redhat.productinventory.entities.Inventory;
import com.redhat.productinventory.services.ProductInventoryService;

@RestController
@RequestMapping(value = "/inventory")
public class ProductInventoryController {
		
	@Autowired
	ProductInventoryService inventoryService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Inventory saveProduct(@RequestBody Inventory inventory) {
		return inventoryService.addInventory(inventory);
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<InventoryDTO> showProductList(@RequestHeader("Authorization") String token) throws JsonProcessingException, IOException {
		return inventoryService.showInventoryList(token);
	}

	@GetMapping(value="/{invId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public InventoryDTO getProduct(@PathVariable("invId") long invId) throws JsonProcessingException, IOException {
		return inventoryService.getInventory(invId);
	}
	
	@PutMapping(value="/{invId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
	public Inventory updateProduct(@PathVariable("invId") long invId, @RequestBody Inventory inventory) {
		return inventoryService.updateInventory(invId, inventory);
	}
	
    @ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping(value="/{invId}")
	public void deleteProduct(@PathVariable("invId") long invId) {
    	inventoryService.deleteInventory(invId);
	}
}
