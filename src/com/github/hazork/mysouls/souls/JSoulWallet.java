package com.github.hazork.mysouls.souls;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.gson.Gson;

public class JSoulWallet {

    private static final Gson GSON = new Gson();

    private transient SoulWallet obj = null;
    private transient String json = null;

    private String uuid;
    private List<String> souls;

    public static JSoulWallet from(SoulWallet wallet) {
	JSoulWallet jsw = new JSoulWallet();
	jsw.obj = wallet;
	jsw.uuid = wallet.getOwnerId().toString();
	jsw.souls = wallet.souls.stream().map(UUID::toString).collect(Collectors.toList());
	return jsw;
    }

    public static JSoulWallet from(String json) {
	return GSON.fromJson(json, JSoulWallet.class);
    }

    public String getJson() {
	return (Objects.nonNull(json)) ? json : (json = GSON.toJson(this));
    }

    public SoulWallet getWallet() {
	if (Objects.nonNull(obj)) return obj;
	SoulWallet wallet = new SoulWallet(UUID.fromString(uuid));
	wallet.souls = souls.stream().map(UUID::fromString).collect(Collectors.toList());
	return obj = wallet;
    }
}
