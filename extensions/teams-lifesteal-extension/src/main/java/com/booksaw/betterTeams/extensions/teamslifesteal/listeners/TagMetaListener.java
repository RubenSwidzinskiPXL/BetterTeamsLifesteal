package com.booksaw.betterTeams.extensions.teamslifesteal.listeners;

import com.booksaw.betterTeams.Team;
import com.booksaw.betterTeams.customEvents.PlayerJoinTeamEvent;
import com.booksaw.betterTeams.customEvents.PlayerLeaveTeamEvent;
import com.booksaw.betterTeams.customEvents.TeamTagChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class TagMetaListener implements Listener {
    private final boolean setPrefix;
    private final String metaKey;

    public TagMetaListener(FileConfiguration cfg) {
        this.setPrefix = cfg.getBoolean("luckperms.setPrefixToTag", false);
        this.metaKey = cfg.getString("luckperms.metaKey", "team_tag");
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTagChange(TeamTagChangeEvent e) {
        updateAll(e.getTeam());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onJoin(PlayerJoinTeamEvent e) {
        OfflinePlayer op = e.getPlayer();
        if (op.isOnline()) update((Player) op, e.getTeam());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onLeave(PlayerLeaveTeamEvent e) {
        OfflinePlayer op = e.getPlayer();
        if (op.isOnline()) update((Player) op, null);
    }

    private void updateAll(Team t) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            Team pt = Team.getTeam(p);
            if ((t == null && pt == null) || (t != null && t.equals(pt))) {
                update(p, pt);
            }
        }
    }

    private void update(Player p, Team t) {
        String tag = t == null ? "" : t.getOriginalTag();
        if (Bukkit.getPluginManager().isPluginEnabled("LuckPerms")) {
            try {
                net.luckperms.api.LuckPerms lp = net.luckperms.api.LuckPermsProvider.get();
                net.luckperms.api.model.user.User user = lp.getUserManager().getUser(p.getUniqueId());
                if (user != null) {
                    // Clear existing meta nodes for team_tag
                    user.data().clear(net.luckperms.api.node.NodeType.META.predicate(m -> m.getMetaKey().equalsIgnoreCase(metaKey)));
                    if (tag != null && !tag.isEmpty()) {
                        user.data().add(net.luckperms.api.node.types.MetaNode.builder(metaKey, tag).build());
                        if (setPrefix) {
                            user.data().clear(net.luckperms.api.node.NodeType.PREFIX.predicate(n -> true));
                            user.data().add(net.luckperms.api.node.types.PrefixNode.builder("[" + tag + "] ", 100).build());
                        }
                    } else if (setPrefix) {
                        user.data().clear(net.luckperms.api.node.NodeType.PREFIX.predicate(n -> true));
                    }
                    lp.getUserManager().saveUser(user);
                }
            } catch (Throwable ignored) {
            }
        }
    }
}
