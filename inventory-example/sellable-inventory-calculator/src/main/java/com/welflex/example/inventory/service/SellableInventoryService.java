package com.welflex.example.inventory.service;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

import com.welflex.example.inventory.exception.NotFoundException;

import io.welflex.examples.inventory.LocationSku;

public interface SellableInventoryService {
  
  @AutoProperty
  static class HostPort {
	private final String host;
	private final int port;

	public HostPort(String host, int port) {
	  this.host = host;
	  this.port = port;
	}

	public String getHost() {
	  return host;
	}

	public int getPort() {
	  return port;
	}
	
	@Override
	public String toString() {
	  return String.format("%s:%s", host, port);
	}

	@Override
	public int hashCode() {
	  return Pojomatic.hashCode(this);
	}

	@Override
	public boolean equals(Object other) {
	  return Pojomatic.equals(this, other);
	}
  }
  
  HostPort hostPortForKey(LocationSku locationSku) throws NotFoundException;
  
  Integer getSellableInventory(LocationSku locationSku) throws NotFoundException;
}
