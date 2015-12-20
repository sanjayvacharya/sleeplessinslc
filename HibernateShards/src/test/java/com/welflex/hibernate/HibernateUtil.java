package com.welflex.hibernate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.shards.ShardId;
import org.hibernate.shards.ShardedConfiguration;
import org.hibernate.shards.cfg.ConfigurationToShardConfigurationAdapter;
import org.hibernate.shards.cfg.ShardConfiguration;
import org.hibernate.shards.loadbalance.RoundRobinShardLoadBalancer;
import org.hibernate.shards.strategy.ShardStrategy;
import org.hibernate.shards.strategy.ShardStrategyFactory;
import org.hibernate.shards.strategy.ShardStrategyImpl;
import org.hibernate.shards.strategy.access.SequentialShardAccessStrategy;
import org.hibernate.shards.strategy.access.ShardAccessStrategy;
import org.hibernate.shards.strategy.resolution.AllShardsShardResolutionStrategy;
import org.hibernate.shards.strategy.resolution.ShardResolutionStrategy;
import org.hibernate.shards.strategy.selection.ShardSelectionStrategy;

import com.welflex.model.NationalPlayer;

public class HibernateUtil {
  private static final SessionFactory sessionFactory;

  static {
    try {
      AnnotationConfiguration config = (AnnotationConfiguration) new AnnotationConfiguration()
          .configure("hibernate0.cfg.xml");

      config.addAnnotatedClass(NationalPlayer.class);
      
      List<ShardConfiguration> shardConfigs = new ArrayList<ShardConfiguration>();
      
      shardConfigs.add(buildShardConfig("hibernate0.cfg.xml"));
      shardConfigs.add(buildShardConfig("hibernate1.cfg.xml"));
      shardConfigs.add(buildShardConfig("hibernate2.cfg.xml"));
     
      ShardStrategyFactory shardStrategyFactory = buildShardStrategyFactory();
      ShardedConfiguration shardedConfig = new ShardedConfiguration(config, shardConfigs,
          shardStrategyFactory);
      
      sessionFactory = shardedConfig.buildShardedSessionFactory();

    }
    catch (Throwable ex) {
      // Make sure you log the exception, as it might be swallowed
      System.err.println("Initial SessionFactory creation failed." + ex);
      throw new ExceptionInInitializerError(ex);
    }
  }

  private static ShardStrategyFactory buildShardStrategyFactory() {
    ShardStrategyFactory shardStrategyFactory = new ShardStrategyFactory() {
      public ShardStrategy newShardStrategy(List<ShardId> shardIds) {
        RoundRobinShardLoadBalancer loadBalancer = new RoundRobinShardLoadBalancer(shardIds);
        ShardSelectionStrategy pss = new com.welflex.hibernate.ShardSelectionStrategy(loadBalancer);
        ShardResolutionStrategy prs = new AllShardsShardResolutionStrategy(shardIds);
        ShardAccessStrategy pas = new SequentialShardAccessStrategy();

        return new ShardStrategyImpl(pss, prs, pas);
      }
    };
    return shardStrategyFactory;
  }

  private static ShardConfiguration buildShardConfig(String configFile) {
    Configuration config = new Configuration().configure(configFile);
    return new ConfigurationToShardConfigurationAdapter(config);
  }

  public static Session getSession() {
    return sessionFactory.openSession();
  }

  public static SessionFactory getSessionFactory() {
    return sessionFactory;
  }
  
  private static Map<Integer, SessionFactory> shardDbFactoryMap = new HashMap<Integer, SessionFactory>();

  static {
    for (int i = 0; i < 3; i++) {
      
      AnnotationConfiguration config = (AnnotationConfiguration) new AnnotationConfiguration().configure("hibernate" + i + ".cfg.xml");
      config.addAnnotatedClass(NationalPlayer.class);
      
      shardDbFactoryMap.put(i, config.buildSessionFactory());      
    }
  }
  
  /**
   * Gets the Session of the particular database in the shard.
   * 
   * @param shardId Shard Id
   * @return        Sharded Database session.
   */
  public static Session getShardDbSession(Integer shardId) {
    return shardDbFactoryMap.get(shardId).openSession();
  }
}
