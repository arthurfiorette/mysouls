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

import lombok.Getter;
import lombok.NonNull;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class WalletStorage extends LoadingStorage<UUID, Wallet, String>
    implements IdentifiableAdapter<Wallet, String>, PlayerAdapter<Wallet, String> {

  @Getter
  @NonNull
  private final MySouls basePlugin;

  private final ConfigFile config;

  private final Gson gson = new GsonBuilder().disableHtmlEscaping()
      .excludeFieldsWithoutExposeAnnotation().disableInnerClassSerialization()
      .registerTypeAdapter(UUID.class, new UuidAdapter()).create();

  public WalletStorage(final MySouls plugin) {
    super(plugin.getComponent(WalletDatabase.class), b -> {
      final ConfigFile file = plugin.getComponent(ConfigFile.class);

      final TimeUnit unit = TimeUnit
          .valueOf(file.getString(Config.CACHE_EVICTION_UNIT).toUpperCase());
      final long duration = file.getLong(Config.CACHE_EVICTION_DURATION);
      final long maximumSize = file.getLong(Config.CACHE_MAX_ENTITIES);
      final int concurrencyLevel = file.getInt(Config.CACHE_CONCURRENCY_LEVEL);

      b.expireAfterWrite(duration, unit);
      b.maximumSize(maximumSize);
      b.concurrencyLevel(concurrencyLevel);

      return b;
    });
    this.basePlugin = plugin;
    this.config = plugin.getComponent(ConfigFile.class);
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
