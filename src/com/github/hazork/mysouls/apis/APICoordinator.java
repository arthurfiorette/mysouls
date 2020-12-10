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
		    final String msg = "A api (%s) não pode ser carregada e é dependente total. Plugin desabilitado";
		    MySouls.log(Level.WARNING, String.format(msg, api.getName()));
		    MySouls.disable();
		    break;

		case SOFT_DEPEND:
		    final String messasge = "A api (%s) não foi encontrada e não será inicializada, mas não era essencial.";
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
