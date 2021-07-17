package com.github.arthurfiorette.mysouls.storage;

import java.util.Arrays;

public class SqlList {

  private static final String TABLE_NAME = "ms_wallets";
  private static final String WALLET_NAME = "wallet";
  private static final String UUID_NAME = "uuid";

  public static final String CREATE_TABLE = String.format(
    "CREATE TABLE IF NOT EXISTS %s (%s varchar(36) UNIQUE NOT NULL, %s TEXT NOT NULL)",
    SqlList.TABLE_NAME,
    SqlList.UUID_NAME,
    SqlList.WALLET_NAME
  );

  public static final String INSERT = String.format(
    "INSERT OR REPLACE INTO %s (%s, %s) VALUES (?, ?)",
    SqlList.TABLE_NAME,
    SqlList.UUID_NAME,
    SqlList.WALLET_NAME
  );

  public static final String SELECT_ALL = String.format(
    "SELECT %s FROM %s",
    SqlList.WALLET_NAME,
    SqlList.TABLE_NAME
  );

  public static final String SELECT = String.format(
    "SELECT %s FROM %s WHERE %s = ?",
    SqlList.WALLET_NAME,
    SqlList.TABLE_NAME,
    SqlList.UUID_NAME
  );

  public static final String selectMany(final String... keys) {
    return String.format(
      "SELECT %s FROM %s WHERE %s IN (%s)",
      SqlList.WALLET_NAME,
      SqlList.TABLE_NAME,
      SqlList.UUID_NAME,
      String.join("', '", Arrays.asList(keys))
    );
  }
}
