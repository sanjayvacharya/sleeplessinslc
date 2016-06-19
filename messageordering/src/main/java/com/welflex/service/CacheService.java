package com.welflex.service;

/**
 * A Cache Service
 */
public interface CacheService {

  /**
   * Some Action to execute with the Cache
   * 
   * @param <T>
   * @param <E>
   */
  static interface CacheAction<T, E extends Exception> {
    public T doAction() throws E;
  }

  /**
   * Perform an action with the Cache with the specified cache key by safely releasing the lock
   * acquired after completion.
   * 
   * @param <T> Type of Return
   * @param <E> Exception type
   * @param cacheKey Key for the cache
   * @param timeStamp Timestamp of the message
   * @param action A Cache Action
   * @return A result of the action
   * @throws E If there is an error
   */
  <T, E extends Exception> T doCacheAction(String cacheKey, Long timeStamp, CacheAction<T, E> action) throws E;

  /**
   * Shutdown the Cache
   */
  void shutdown();
}
