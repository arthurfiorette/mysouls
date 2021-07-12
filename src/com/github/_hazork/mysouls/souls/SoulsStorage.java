package com.github._hazork.mysouls.souls;

import com.github._hazork.mysouls.data.SoulsDatabase;
import com.github.arthurfiorette.sinklibrary.data.database.Database;
import com.github.arthurfiorette.sinklibrary.data.database.MemoryDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SoulsStorage extends CacheStorage<SoulAccount> {

  public static void main(final String[] args) {
    final SoulsStorage storage = new SoulsStorage(new MemoryDatabase<>());

    final SoulAccount acc = new SoulAccount(UUID.randomUUID());

    final UUID id1 = UUID.randomUUID();
    System.out.println("Id 1:" + id1);
    acc.wallet.put(id1, new PlayerSoul(id1, 4));

    final UUID id2 = UUID.randomUUID();
    System.out.println("Id 2:" + id2);
    acc.wallet.put(id2, new PlayerSoul(id2, 0));

    final JsonObject obj = storage.serialize(acc);

    final SoulAccount deserial = storage.deserialize(obj);

    System.out.println(SoulsStorage.gson.toJson(acc.wallet));
    System.out.println(SoulsStorage.gson.toJson(deserial.wallet));
  }

  // private Gson gson = new Gson();
  private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

  private static String ID_NAME = "ownerId";
  private static String SOULS_NAME = "souls";

  public SoulsStorage(final Database<JsonObject> database) {
    super(database, options -> options.expireAfterAccess(5, TimeUnit.SECONDS));
  }

  public SoulAccount get(final UUID uuid) {
    return this.get(uuid.toString());
  }

  @Override
  public JsonObject serialize(final SoulAccount acc) {
    if (acc.playerCount() == 0) {
      return SoulsDatabase.EMPTY;
    }
    final JsonObject obj = new JsonObject();
    obj.addProperty(SoulsStorage.ID_NAME, FastUUID.toString(acc.getOwnerId()));
    obj.add(SoulsStorage.SOULS_NAME, SoulsStorage.gson.toJsonTree(acc.getSouls()));
    return obj;
  }

  @Override
  public SoulAccount deserialize(final JsonObject raw) {
    final UUID uuid = FastUUID.parseUUID(raw.get(SoulsStorage.ID_NAME).getAsString());
    final JsonArray soulsArray = new Gson()
      .fromJson(raw.get(SoulsStorage.SOULS_NAME), JsonArray.class);
    final SoulAccount acc = new SoulAccount(uuid);
    acc.wallet =
      StreamSupport
        .stream(soulsArray.spliterator(), false)
        .map(e -> SoulsStorage.gson.fromJson(e, PlayerSoul.class))
        .collect(Collectors.toMap(PlayerSoul::getOwnerId, e -> e));
    return acc;
  }
}
