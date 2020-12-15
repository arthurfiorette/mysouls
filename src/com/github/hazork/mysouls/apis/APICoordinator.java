package com.github.hazork.mysouls.apis;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import com.github.hazork.mysouls.MySouls;

public class APICoordinator {

    private Set<MySoulsAPI> apis = new HashSet<>();

    public boolean registerApi(MySoulsAPI... msapis) {
	boolean result = false;
	for (MySoulsAPI api : msapis) {
	    result |= apis.add(api);
	}
	return result;
    }

    public void enable() {
	for (MySoulsAPI api : apis) {
	    if (api.canRegister()) {
		api.register();
		continue;
	    }
	    switch (api.getDependent()) {
		case DEPEND:
		    final String msg = "The api (%s) cannot be loaded and is fully dependent. Plugin disabled";
		    MySouls.log(Level.WARNING, String.format(msg, api.getName()));
		    MySouls.disable();
		    break;

		case SOFT_DEPEND:
		    final String messasge = "The api (%s) was not found and will not be initialized, but it was not essential.";
		    MySouls.log(Level.INFO, String.format(messasge, api.getName()));
		    break;
	    }
	}
    }

    public void disable() {
	for (MySoulsAPI api : apis) {
	    if (api.isRegistered()) {
		api.unregister();
	    }
	}
    }
}
