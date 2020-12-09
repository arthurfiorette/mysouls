package com.github.hazork.mysouls.apis.implementations;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import com.github.hazork.mysouls.MySouls;
import com.github.hazork.mysouls.apis.MySoulsAPI;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PlaceholderAPI extends PlaceholderExpansion implements MySoulsAPI {

    private MySouls mysouls = MySouls.get();

    @Override
    public boolean canRegister() {
	return true;
    }

    @Override
    public boolean persist() {
	return true;
    }

    @Override
    public @NotNull String getAuthor() {
	return mysouls.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getIdentifier() {
	return MySouls.NAME;
    }

    @Override
    public @NotNull String getVersion() {
	return MySouls.getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
	if (player == null) return null;
	String[] arr = params.split("_");
	String[] args = Arrays.copyOfRange(arr, 1, arr.length);
	Object result = null;
	switch (arr[0]) {
	    case "soulscount":
		if (args.length < 1) result = MySouls.getDB().from(player).soulsCount();
		else {
		    @SuppressWarnings("deprecation")
		    OfflinePlayer of = Bukkit.getOfflinePlayer(args[0]);
		    result = MySouls.getDB().from(player).soulsCount(of.getUniqueId());
		}
		break;

	    case "playercount":
		result = MySouls.getDB().from(player).playerCount();
		break;

	    case "soulsratio":
		result = MySouls.getDB().from(player).soulsRatio();
		break;

	    default:
		return null;
	}
	return result.toString();
    }
}
