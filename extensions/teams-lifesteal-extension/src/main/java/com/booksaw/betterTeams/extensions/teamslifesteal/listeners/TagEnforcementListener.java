package com.booksaw.betterTeams.extensions.teamslifesteal.listeners;

import com.booksaw.betterTeams.PlayerRank;
import com.booksaw.betterTeams.TeamPlayer;
import com.booksaw.betterTeams.customEvents.TeamTagChangeEvent;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.List;

public class TagEnforcementListener implements Listener {
    private final String pattern;
    private final boolean uppercase;
    private final String denyMessage;

    public TagEnforcementListener(FileConfiguration cfg) {
        this.pattern = cfg.getString("clanTag.pattern", "[A-Z0-9]{1,5}");
        this.uppercase = cfg.getBoolean("clanTag.enforceUppercase", true);
        this.denyMessage = ChatColor.translateAlternateColorCodes('&', cfg.getString("clanTag.denyMessage", "&cInvalid tag"));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onTagChange(TeamTagChangeEvent e) {
        String tag = e.getNewTeamTag();
        if (tag == null) return;
        if (uppercase) tag = tag.toUpperCase();
        if (!tag.matches(pattern)) {
            e.setCancelled(true);
            // Notify the owner(s)
            List<TeamPlayer> owners = e.getTeam().getRank(PlayerRank.OWNER);
            if (!owners.isEmpty()) {
                TeamPlayer owner = owners.get(0);
                if (owner.getPlayer() != null && owner.getPlayer().isOnline()) {
                    owner.getPlayer().getPlayer().sendMessage(denyMessage);
                }
            }
            return;
        }
        // Apply normalized form
        e.setNewTeamTag(tag);
    }
}
