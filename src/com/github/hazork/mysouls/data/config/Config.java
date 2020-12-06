package com.github.hazork.mysouls.data.config;

public enum Config {

    ;
    private String path;

    Config(String path) {
	this.path = path;
    }

    public String getPath() {
	return path;
    }

}
