package com.welflex.activemq;

import java.util.Random;

import org.apache.activemq.ActiveMQConnectionFactory;


public class Util {
  // Restricted Record Identifiers
  public static final long[] RECORD_IDS = {10, 12, 13};
  private final static Random random = new Random();
  
  /**
   * @return  A Random record id
   */
  public static Long createRandomRecordId() {
    return RECORD_IDS[random.nextInt(RECORD_IDS.length)];
  }
  
  /**
   * An Active MQ Connection factory
   * 
   * @param connectionName
   * @return
   */
  public static ActiveMQConnectionFactory createConnectionFactory(String connectionName) {
    return new ActiveMQConnectionFactory("foo", "password", "vm://localhost");
  }
}