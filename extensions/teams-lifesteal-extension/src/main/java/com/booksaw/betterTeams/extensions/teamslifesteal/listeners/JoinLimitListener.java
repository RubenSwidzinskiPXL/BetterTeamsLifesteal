package com.booksaw.betterTeams.extensions.teamslifesteal.listeners;

import com.booksaw.betterTeams.Team;
import com.booksaw.betterTeams.customEvents.PlayerJoinTeamEvent;
import com.booksaw.betterTeams.extensions.teamslifesteal.services.storage.AdminStore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class JoinLimitListener implements Listener {
    private final AdminStore store;

    public JoinLimitListener(AdminStore store) {
        this.store = store;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onJoin(PlayerJoinTeamEvent e) {
        Team t = e.getTeam();
        int override = store.getSizeOverride(t.getID().toString(), -1);
        if (override <= 0) return;
        int onlineMembers = t.getMembers().size();
        if (onlineMembers >= override) {
            e.setCancelled(true);
            if (e.getPlayer().isOnline()) {
                e.getPlayer().getPlayer().sendMessage("§cTeam is at max capacity (" + override + ")");
            }
        }
    }
}
