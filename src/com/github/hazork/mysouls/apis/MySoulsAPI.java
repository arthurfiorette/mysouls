package com.github.hazork.mysouls.apis;

public interface MySoulsAPI {

    String getName();

    boolean canRegister();

    boolean register();

    boolean unregister();

    boolean isRegistered();

    default Dependent getDependent() {
	return Dependent.SOFT_DEPEND;
    }

    public enum Dependent {
	SOFT_DEPEND,
	DEPEND;
    }
}
