package com.welflex.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

public class CacheManager {
  private static final Logger LOG = Logger.getLogger(CacheManager.class);

  private static Map<String, CacheEntry> objCache = new ConcurrentHashMap<String, CacheEntry>();

  public static void cache(String key, CacheEntry entry) {
    LOG.debug("Caching Object with key [" + key + "]");
    objCache.put(key, entry);
  }

  public static CacheEntry get(String key) {
    LOG.debug("Getting Object from Cache for Key:" + key);
    CacheEntry entry =  objCache.get(key);
    LOG.debug("Object in Cache for Key [" + key + "] is :" + entry);

    return entry;
  }

  public static void remove(String key) {
    objCache.remove(key);
  }
}
