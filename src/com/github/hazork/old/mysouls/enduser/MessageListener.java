package com.github.hazork.old.mysouls.enduser;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.github.hazork.old.mysouls.events.SoulChangeEvent;
import com.github.hazork.old.mysouls.events.SoulWithdrawEvent;

public class MessageListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSoulChange(SoulChangeEvent event) {

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSoulWithdraw(SoulWithdrawEvent event) {

    }

}
