package com.booksaw.betterTeams.extensions.teamslifesteal.sidebar;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SidebarPlayerListener implements Listener {
    private final SidebarManager manager;

    public SidebarPlayerListener(SidebarManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        manager.updateFor(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        // No-op; periodic task will clean up
    }
}
