package com.welflex.client;

import java.util.Date;

import javax.ws.rs.core.EntityTag;

public class CacheEntry {
  private final Object o;
  private final Date cacheTillDate;
  private final EntityTag etag;

  public CacheEntry(Object o, EntityTag etag, Date cacheUntil) {
    this.o = o;
    this.etag = etag;
    this.cacheTillDate = cacheUntil;
  }

  public Object getObject() {
    return o;
  }

  public Date getCacheTillDate() {
    return cacheTillDate;
  }

  public EntityTag getEtag() {
    return etag;
  }
}
