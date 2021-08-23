package com.github.arthurfiorette.mysouls.config;

import com.github.arthurfiorette.sinklibrary.config.addons.PathResolver;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
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

  DATABASE_FILENAME("internal.database.filename"),
  DATABASE_TABLE_NAME("internal.database.table.name"),

  DATABASE_COLUMNS_ID("internal.database.table.columns.id"),
  DATABASE_COLUMNS_WALLET("internal.database.table.columns.wallet");

  @Getter
  @NonNull
  private final String path;

}
