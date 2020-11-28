package com.github.hazork.mysouls.utils;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public abstract class Event extends org.bukkit.event.Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled;

    @Override
    public HandlerList getHandlers() {
	return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
	return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
	cancelled = cancel;
    }

}