package com.welflex.example.inventory.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.welflex.example.inventory.model.InventoryByLocationSku;

@Repository
public interface InventoryByLocationSkuRepository extends CassandraRepository<InventoryByLocationSku> {
}
