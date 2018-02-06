package com.welflex.example.inventory.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.core.CassandraAdminOperations;
import org.springframework.data.cassandra.core.CassandraAdminTemplate;

import com.datastax.driver.core.Session;
import com.welflex.example.inventory.model.InventoryByLocationSku;

@Configuration
public class CassandraConfig extends AbstractCassandraConfiguration {
  public static String CASSANDRA_KEY_SPACE = "sellableInventory";

  @Override
  public String getKeyspaceName() {
    return CASSANDRA_KEY_SPACE;
  }

  @Bean
  public CassandraAdminOperations cassandraAdminOperations(Session session) {
    return new CassandraAdminTemplate(session, new MappingCassandraConverter());
  }

  @Override
  public String[] getEntityBasePackages() {
    return new String[] { InventoryByLocationSku.class.getPackage().getName() };
  }

  @Override
  public SchemaAction getSchemaAction() {
    return SchemaAction.CREATE_IF_NOT_EXISTS;
  }

  @Value("${spring.data.cassandra.contact-points}")
  private String cassandraContactPoints;

  @Override
  protected String getContactPoints() {
    return cassandraContactPoints;
  }
}
