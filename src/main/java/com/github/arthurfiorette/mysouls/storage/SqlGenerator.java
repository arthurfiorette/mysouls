package com.github.arthurfiorette.mysouls.storage;

import com.github.arthurfiorette.mysouls.config.Config;
import com.github.arthurfiorette.mysouls.config.ConfigFile;
import com.github.arthurfiorette.sinklibrary.interfaces.BaseComponent;
import com.github.arthurfiorette.sinklibrary.interfaces.BasePlugin;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SqlGenerator implements BaseComponent {

  @Getter
  @NonNull
  private final BasePlugin basePlugin;

  public String tableName() {
    return this.getConfig().getString(Config.DATABASE_TABLE_NAME);
  }

  public String idColumnName() {
    return this.getConfig().getString(Config.DATABASE_COLUMNS_ID);
  }

  public String walletColumnName() {
    return this.getConfig().getString(Config.DATABASE_COLUMNS_WALLET);
  }

  public String createTableSql() {
    return String.format(
      "CREATE TABLE IF NOT EXISTS %s (%s varchar(36) UNIQUE NOT NULL, %s TEXT NOT NULL)",
      this.tableName(),
      this.idColumnName(),
      this.walletColumnName()
    );
  }

  public String insertWalletSql() {
    return String.format(
      "INSERT OR REPLACE INTO %s (%s, %s) VALUES (?, ?)",
      this.tableName(),
      this.idColumnName(),
      this.walletColumnName()
    );
  }

  public String selectAllSql() {
    return String.format("SELECT %s FROM %s", this.walletColumnName(), this.tableName());
  }

  public String selectSql() {
    return String.format(
      "SELECT %s FROM %s WHERE %s = ?",
      this.walletColumnName(),
      this.tableName(),
      this.idColumnName()
    );
  }

  public String selectManySql(final String... keys) {
    return String.format(
      "SELECT %s FROM %s WHERE %s IN (%s)",
      this.walletColumnName(),
      this.tableName(),
      this.idColumnName(),
      String.join(", ", keys)
    );
  }

  private ConfigFile getConfig() {
    return this.basePlugin.getComponent(ConfigFile.class);
  }
}
