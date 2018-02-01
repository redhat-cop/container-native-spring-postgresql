package com.redhat.productinventory.services;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.productinventory.dtos.InventoryDTO;
import com.redhat.productinventory.entities.Inventory;
import com.redhat.productinventory.interfaces.ProductInventory;
import com.redhat.productinventory.repositories.InventoryRepository;

@Service
public class ProductInventoryService implements ProductInventory {

	@Autowired
	InventoryRepository inventoryRepository;

	@Value("${service.catalog.name}")
	private String productServiceName;

	@Autowired
	RestTemplate restTemplate;

	@Override
	public List<InventoryDTO> showInventoryList(String token) throws JsonProcessingException, IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", token);
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<?> requestheaders = new HttpEntity<>(headers);
		List<Inventory> invList = inventoryRepository.findAll();
		List<InventoryDTO> inventory = new ArrayList<>();
		for(Inventory inv : invList) {
			InventoryDTO invDTO = new InventoryDTO();
			invDTO.setInvId(inv.getInvId());
			invDTO.setItemId(inv.getItemId());
			invDTO.setQty(inv.getQty());
			ResponseEntity<String> response = restTemplate.exchange(getURI() + "/" + inv.getItemId(), HttpMethod.GET, requestheaders, String.class);
			JsonNode rootNode = new ObjectMapper().readTree(response.getBody());
			invDTO.setName(rootNode.get("name").textValue());
			inventory.add(invDTO);
		}
		return inventory;
	}

	@Override
	public void deleteInventory(long invId) {
		// TODO Auto-generated method stub

	}

	@Override
	public InventoryDTO getInventory(long invId) throws JsonProcessingException, IOException {
		Inventory inv = inventoryRepository.findOne(invId);
		InventoryDTO invDTO = new InventoryDTO();
		invDTO.setInvId(inv.getInvId());
		invDTO.setItemId(inv.getItemId());
		invDTO.setQty(inv.getQty());
		ResponseEntity<String> response = restTemplate.getForEntity(getURI() + "/" + inv.getItemId(), String.class);
		JsonNode rootNode = new ObjectMapper().readTree(response.getBody());
		invDTO.setName(rootNode.get("name").textValue());
		return invDTO;
	}

	@Override
	public Inventory addInventory(Inventory inventory) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Inventory updateInventory(long invId, Inventory inventory) {
		// TODO Auto-generated method stub
		return null;
	}

	private URI getURI() {
		StringBuilder builder = new StringBuilder("http://");
		builder.append(productServiceName);
		return URI.create(builder.toString());
	}

}
