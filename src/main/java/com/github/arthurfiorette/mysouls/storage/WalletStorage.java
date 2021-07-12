package com.github.arthurfiorette.mysouls.storage;

import com.github.arthurfiorette.mysouls.MySouls;
import com.github.arthurfiorette.mysouls.config.Config;
import com.github.arthurfiorette.mysouls.config.ConfigFile;
import com.github.arthurfiorette.mysouls.model.Wallet;
import com.github.arthurfiorette.mysouls.util.SoulsExecutor;
import com.github.arthurfiorette.sinklibrary.data.storage.LoadingStorage;
import com.github.arthurfiorette.sinklibrary.data.storage.addons.IdentifiableAdapter;
import com.github.arthurfiorette.sinklibrary.data.storage.addons.PlayerAdapter;
import com.github.arthurfiorette.sinklibrary.interfaces.BasePlugin;
import com.github.arthurfiorette.sinklibrary.uuid.UuidAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class WalletStorage
  extends LoadingStorage<UUID, Wallet, String>
  implements IdentifiableAdapter<Wallet, String>, PlayerAdapter<Wallet, String> {

  private final MySouls plugin;
  private final ConfigFile config;

  private final Gson gson = new GsonBuilder()
    .disableHtmlEscaping()
    .excludeFieldsWithoutExposeAnnotation()
    .disableInnerClassSerialization()
    .registerTypeAdapter(UUID.class, new UuidAdapter())
    .create();

  public WalletStorage(final MySouls plugin) {
    super(
      plugin.getComponent(WalletDatabase.class),
      plugin.getComponent(SoulsExecutor.class),
      b -> b.expireAfterAccess(5, TimeUnit.MINUTES).maximumSize(256)
    );
    this.plugin = plugin;
    this.config = plugin.getComponent(ConfigFile.class);
  }

  @Override
  public void enable() throws Exception {
    super.enable();
  }

  public void setJson(final UUID id, final String value) {
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

  @Override
  public BasePlugin getBasePlugin() {
    return this.plugin;
  }
}
