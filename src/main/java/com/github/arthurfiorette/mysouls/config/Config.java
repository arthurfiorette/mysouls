package com.github.arthurfiorette.mysouls.config;

import com.github.arthurfiorette.sinklibrary.config.addons.PathResolver;

public enum Config implements PathResolver {

  // Rules
  INITIAL_SOULS("rules.souls.initial"),

  // Customization
  COIN_HEAD_URL("customization.heads.coin"),
  SOUL_HEAD_URL("customization.heads.soul"),
  TROPHY_HEAD_URL("customization.heads.trophy"),

  // Internal
  CACHE_MAX_ENTITIES("internal.cache.max-entities"),
  CACHE_CONCURRENCY_LEVEL("internal.cache.concurrency-level"),
  CACHE_EVICTION_UNIT("internal.cache.eviction.unit"),
  CACHE_EVICTION_DURATION("internal.cache.eviction.duration"),

  ;

  private String path;

  Config(final String path) {
    this.path = path;
  }

  @Override
  public String getPath() {
    return this.path;
  }
}
