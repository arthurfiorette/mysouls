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
	    if (api.canRegister()) api.register();
	    else switch (api.getDependent()) {
		case DEPEND: {
		    MySouls.log(Level.WARNING,
			    String.format("A api (%s) não pode ser carregada e é dependente total. Plugin desabilitado",
				    api.getName()));
		    MySouls.disable();
		    break;
		}

		case SOFT_DEPEND: {
		    MySouls.log(Level.INFO,
			    String.format(
				    "A api (%s) não foi encontrada e não será inicializada, mas não era essencial.",
				    api.getName()));
		    break;
		}
	    }
	}
    }

    public void disable() {
	for (MySoulsAPI api : apis) if (api.isRegistered()) api.unregister();
    }

}
