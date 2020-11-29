package com.github.hazork.mysouls.apis;

import org.bukkit.entity.Player;

import com.github.hazork.mysouls.MySouls;
import com.github.hazork.mysouls.util.Spigots;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PlaceholderAPI extends PlaceholderExpansion implements MSApi {

    private MySouls plugin;

    public PlaceholderAPI(MySouls plugin) {
	this.plugin = plugin;
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
	if (player == null) return null;
	switch (params) {
//	    case "souls":
//		return SoulCoordinator.getSouls(player).toString();
//
//	    case "canlose":
//		return SoulCoordinator.getSoul(player).canLoseSoul() ? "§aSim" : "§cNão";

	    default:
		return null;
	}
    }

    @Override
    public boolean detect() {
	return Spigots.hasPlugin("PlaceholderAPI");
    }

    @Override
    public boolean register() {
	if (detect()) return super.register();
	else return false;
    }

    @Override
    public boolean persist() {
	return true;
    }

    @Override
    public boolean canRegister() {
	return true;
    }

    @Override
    public String getAuthor() {
	return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    public String getIdentifier() {
	return "hsouls";
    }

    @Override
    public String getVersion() {
	return "1.0.0";
    }

}