package com.redhat.productinventory.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.redhat.productinventory.entities.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

}
