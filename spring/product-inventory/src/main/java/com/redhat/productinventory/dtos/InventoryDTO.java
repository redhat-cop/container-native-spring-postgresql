package com.redhat.productinventory.dtos;

public class InventoryDTO {
	
	private Long invId;
	
	private Integer qty;
	
	private Long itemId;
	
	private String name;
	
	public InventoryDTO() {}

	public Long getInvId() {
		return invId;
	}

	public void setInvId(Long invId) {
		this.invId = invId;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
