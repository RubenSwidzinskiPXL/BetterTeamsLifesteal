package com.booksaw.betterTeams.extensions.teamslifesteal.listeners;

import com.booksaw.betterTeams.Team;
import com.booksaw.betterTeams.extension.BetterTeamsExtension;
import com.booksaw.betterTeams.extensions.teamslifesteal.services.EconomyService;
import com.booksaw.betterTeams.extensions.teamslifesteal.services.TeamService;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import com.booksaw.betterTeams.extensions.teamslifesteal.services.storage.AdminStore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TeamWarpListener implements Listener {
    private final TeamService teamService;
    private final EconomyService economyService;
    private final BetterTeamsExtension ext;

    private final int freePerDay;
    private final double cost;
    private final List<String> payOrder;
    private final String denyMessage;
    private final String msgFree;
    private final String msgPaidTeam;
    private final String msgPaidPlayer;

    private final AdminStore store;

    public TeamWarpListener(FileConfiguration cfg, TeamService teamService, EconomyService economyService, BetterTeamsExtension ext) {
        this.teamService = teamService;
        this.economyService = economyService;
        this.ext = ext;
        ConfigurationSection s = cfg.getConfigurationSection("warp");
        this.freePerDay = s.getInt("freePerDay", 3);
        this.cost = s.getDouble("cost", 250.0);
        this.payOrder = s.getStringList("payOrder");
        if (this.payOrder.isEmpty()) this.payOrder.addAll(Arrays.asList("TEAM","PLAYER"));
        this.denyMessage = color(s.getString("denyMessage", "&cNot enough free warps/funds to warp."));
        ConfigurationSection m = s.getConfigurationSection("useMessages");
        this.msgFree = color(m.getString("free", "&aFree team warp used: &e%used%/%limit%"));
        this.msgPaidTeam = color(m.getString("paidTeam", "&aCharged team bank &e$%amount% &7for warp"));
        this.msgPaidPlayer = color(m.getString("paidPlayer", "&aCharged your balance &e$%amount% &7for warp"));

        this.store = new AdminStore(ext);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onTeamWarpCommand(PlayerCommandPreprocessEvent e) {
        String msg = e.getMessage().toLowerCase(Locale.ROOT).trim();
        if (!(msg.startsWith("/team warp ") || msg.equals("/team warp") || msg.startsWith("/t warp ") || msg.equals("/t warp"))) {
            return;
        }
        Player p = e.getPlayer();
        Team team = teamService.getTeam(p);
        if (team == null) return; // not in a team, let BT handle message

        String teamId = teamService.getTeamId(team);
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Integer override = store.getFreeWarpsOverride(teamId);
        int limit = override != null ? override : freePerDay;
        int used = store.getWarpUsedToday(teamId, today);

        if (used < limit) {
            used++;
            store.setWarpUsedToday(teamId, today, used);
            p.sendMessage(msgFree.replace("%used%", String.valueOf(used)).replace("%limit%", String.valueOf(limit)));
            return; // allow through
        }

        // No free uses left: attempt payment
        boolean paid = false;
        for (String mode : payOrder) {
            if (mode.equalsIgnoreCase("TEAM")) {
                if (team.getMoney() >= cost) {
                    if (economyService.withdrawTeam(team, cost)) {
                        paid = true;
                        p.sendMessage(msgPaidTeam.replace("%amount%", String.format(Locale.US, "%.2f", cost)));
                        break;
                    }
                }
            } else if (mode.equalsIgnoreCase("PLAYER")) {
                if (economyService.hasEconomy() && economyService.getBalance(p) >= cost) {
                    if (economyService.withdraw(p, cost)) {
                        paid = true;
                        p.sendMessage(msgPaidPlayer.replace("%amount%", String.format(Locale.US, "%.2f", cost)));
                        break;
                    }
                }
            }
        }

        if (!paid) {
            e.setCancelled(true);
            p.sendMessage(denyMessage);
        }
    }

    private static String color(String s) { return ChatColor.translateAlternateColorCodes('&', s); }
}
