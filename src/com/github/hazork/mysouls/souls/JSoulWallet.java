package com.github.hazork.mysouls.souls;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.gson.Gson;

public class JSoulWallet {

    private static final Gson GSON = new Gson();

    private transient SoulWallet obj = null;
    private transient String json = null;

    private String uuid;
    private Map<String, Integer> souls;

    public static JSoulWallet from(SoulWallet wallet) {
	JSoulWallet jsw = new JSoulWallet();
	jsw.obj = wallet;
	jsw.uuid = wallet.getOwnerId().toString();
	jsw.souls = wallet.souls.entrySet().stream()
		.collect(Collectors.toMap(e -> e.getKey().toString(), Entry::getValue));
	return jsw;
    }

    public static JSoulWallet from(String json) {
	return GSON.fromJson(json, JSoulWallet.class);
    }

    public String getJson() {
	return (Objects.nonNull(json)) ? json : (json = GSON.toJson(this));
    }

    public SoulWallet getWallet() {
	if (Objects.nonNull(obj)) {
	    return obj;
	}
	SoulWallet wallet = new SoulWallet(UUID.fromString(uuid));
	wallet.souls = souls.entrySet().stream()
		.collect(Collectors.toMap(e -> UUID.fromString(e.getKey()), Entry::getValue));
	return obj = wallet;
    }
}
