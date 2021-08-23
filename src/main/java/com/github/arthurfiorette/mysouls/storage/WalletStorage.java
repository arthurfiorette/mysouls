package com.github.arthurfiorette.mysouls.storage;

import com.github.arthurfiorette.mysouls.MySouls;
import com.github.arthurfiorette.mysouls.config.Config;
import com.github.arthurfiorette.mysouls.config.ConfigFile;
import com.github.arthurfiorette.mysouls.model.Wallet;
import com.github.arthurfiorette.sinklibrary.data.storage.LoadingStorage;
import com.github.arthurfiorette.sinklibrary.data.storage.addons.IdentifiableAdapter;
import com.github.arthurfiorette.sinklibrary.data.storage.addons.PlayerAdapter;
import com.github.arthurfiorette.sinklibrary.uuid.UuidAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import lombok.Getter;
import lombok.NonNull;

public class WalletStorage
  extends LoadingStorage<UUID, Wallet, String>
  implements IdentifiableAdapter<Wallet, String>, PlayerAdapter<Wallet, String> {

  @Getter
  @NonNull
  private final MySouls basePlugin;

  private final ConfigFile config;

  private final Gson gson = new GsonBuilder()
    .disableHtmlEscaping()
    .excludeFieldsWithoutExposeAnnotation()
    .disableInnerClassSerialization()
    .registerTypeAdapter(UUID.class, new UuidAdapter())
    .create();

  public WalletStorage(final MySouls plugin) {
    this(plugin, plugin.get(SqliteDatabase.class), plugin.get(ConfigFile.class));
  }

  private WalletStorage(
    final MySouls plugin,
    final SqliteDatabase database,
    final ConfigFile config
  ) {
    super(
      database,
      b -> {
        final String unit = config.getString(Config.CACHE_EVICTION_UNIT);
        final long duration = config.getLong(Config.CACHE_EVICTION_DURATION);
        final long maximumSize = config.getLong(Config.CACHE_MAX_ENTITIES);
        final int concurrencyLevel = config.getInt(Config.CACHE_CONCURRENCY_LEVEL);

        b.expireAfterWrite(duration, TimeUnit.valueOf(unit.toUpperCase()));
        b.maximumSize(maximumSize);
        b.concurrencyLevel(concurrencyLevel);
      }
    );
    this.basePlugin = plugin;
    this.config = config;
  }

  public void loadJson(final UUID id, final String value) {
    this.cache.put(id, this.deserialize(value));
  }

  @Override
  public String serialize(final Wallet object) {
    return this.gson.toJson(object, Wallet.class);
  }

  @Override
  public Wallet deserialize(final String raw) {
    return this.gson.fromJson(raw, Wallet.class);
  }

  @Override
  protected Wallet create(final UUID key) {
    final Wallet wallet = new Wallet(key);

    // Add initial souls
    wallet.addSoul(key, this.config.getInt(Config.INITIAL_SOULS));

    return wallet;
  }
}
