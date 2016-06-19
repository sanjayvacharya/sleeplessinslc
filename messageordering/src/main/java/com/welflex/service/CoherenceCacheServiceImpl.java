package com.welflex.service;

import java.io.IOException;
import java.util.Properties;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.DefaultCacheServer;
import com.tangosol.net.NamedCache;
import com.welflex.exception.StaleObjectException;

public class CoherenceCacheServiceImpl implements CacheService {
  private final NamedCache idCache;

  public CoherenceCacheServiceImpl() {
    Properties props = new Properties();
    try {
      props.load(getClass().getResourceAsStream("/cache.properties"));
      idCache = CacheFactory.getCache("IdCache");
    }
    catch (IOException e) {
      throw new RuntimeException("Unable to instantiate Service");
    }
  }
  
  /**
   * Pesssimistic locking and action based of coherence.
   */
  @Override
  public <T, E extends Exception> T doCacheAction(String cacheKey, Long timeStamp,
    CacheAction<T, E> action) throws E {
    try {
      idCache.lock(cacheKey, -1);
      Long cacheTime = (Long) idCache.get(cacheKey);
      if (cacheTime == null || timeStamp.longValue() > cacheTime.longValue()) {
        T result = action.doAction();
        idCache.put(cacheKey, timeStamp);
        return result;
      }
      throw new StaleObjectException("Coherence Cached Date [" + cacheTime + "] for Record [" + cacheKey + "] is newer than passed in time ["
        + timeStamp + "]. Rejecting the record....");
    }
    finally {
      idCache.unlock(cacheKey);
    }
  }

  public static void main(String args[]) {
    new CoherenceCacheServiceImpl();
  }

  @Override
  public void shutdown() {
    DefaultCacheServer.shutdown();
  }
}
