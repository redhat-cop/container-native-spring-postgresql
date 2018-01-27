package com.redhat.productinventory.interfaces;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.redhat.productinventory.dtos.InventoryDTO;
import com.redhat.productinventory.entities.Inventory;

public interface ProductInventory {
	
	/**
	 * @param invId
	 */
	void deleteInventory(long invId);
	
	/**
	 * @param invId
	 * @return
	 * @throws IOException 
	 * @throws JsonProcessingException 
	 */
	InventoryDTO getInventory(long invId) throws JsonProcessingException, IOException;
	
	/**
	 * @param inventory
	 * @return
	 */
	Inventory addInventory(Inventory inventory);

	/**
	 * @param invId
	 * @param inventory
	 * @return
	 */
	Inventory updateInventory(long invId, Inventory inventory);

	/**
	 * @param principal
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	List<InventoryDTO> showInventoryList(String token) throws JsonProcessingException, IOException;
	
}
