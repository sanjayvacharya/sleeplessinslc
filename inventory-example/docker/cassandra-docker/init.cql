CREATE KEYSPACE IF NOT EXISTS sellableInventory
 WITH REPLICATION = {'class': 'SimpleStrategy', 'replication_factor': 1};

CREATE TABLE sellableinventory.inventory_by_location_sku (
	location text,
	sku text,
	count int,
	last_updated timestamp,
	PRIMARY KEY (( location, sku ))
);
