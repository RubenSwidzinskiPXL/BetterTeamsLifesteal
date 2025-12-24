package com.booksaw.betterTeams.extensions.teamslifesteal.listeners;

import com.booksaw.betterTeams.Team;
import com.booksaw.betterTeams.extension.BetterTeamsExtension;
import com.booksaw.betterTeams.extensions.teamslifesteal.services.TeamService;
import com.booksaw.betterTeams.extensions.teamslifesteal.services.storage.AdminStore;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Locale;

public class AdminCommandListener implements Listener {

    private final BetterTeamsExtension ext;
    private final AdminStore store;
    private final TeamService teams;
    private final String permRoot;
    private final String permReset;
    private final String permSetTag;
    private final String permSetSize;
    private final String permSetFreeWarps;
    private final String tagPattern;

    public AdminCommandListener(BetterTeamsExtension ext, FileConfiguration cfg, TeamService teams) {
        this.ext = ext;
        this.store = new AdminStore(ext);
        this.teams = teams;
        this.permRoot = cfg.getString("admin.permissions.root", "teamslifesteal.admin");
        this.permReset = cfg.getString("admin.permissions.resetWarpUses", "teamslifesteal.admin.resetwarpuses");
        this.permSetTag = cfg.getString("admin.permissions.setTag", "teamslifesteal.admin.settag");
        this.permSetSize = cfg.getString("admin.permissions.setSize", "teamslifesteal.admin.setsize");
        this.permSetFreeWarps = cfg.getString("admin.permissions.setFreeWarps", "teamslifesteal.admin.setfreewarps");
        this.tagPattern = cfg.getString("admin.tag.pattern", "^[A-Z0-9]{1,5}$");
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onAdminCmd(PlayerCommandPreprocessEvent e) {
        String msg = e.getMessage().trim();
        if (!msg.toLowerCase(Locale.ROOT).startsWith("/btls")) return;
        e.setCancelled(true);
        handle(e.getPlayer(), msg.substring(6).trim());
    }

    private void handle(Player sender, String argsLine) {
        if (!sender.hasPermission(permRoot)) {
            sender.sendMessage(color("&cNo permission."));
            return;
        }
        String[] parts = argsLine.split("\\s+");
        if (parts.length == 0 || parts[0].isEmpty()) {
            help(sender);
            return;
        }
        String sub = parts[0].toLowerCase(Locale.ROOT);
        switch (sub) {
            case "resetwarpuses":
                if (!sender.hasPermission(permReset)) { sender.sendMessage(color("&cNo permission.")); return; }
                if (parts.length == 1 || parts[1].equalsIgnoreCase("all")) {
                    store.clearAllWarpUsage();
                    sender.sendMessage(color("&aCleared all warp usage counters."));
                } else {
                    Team t = Team.getTeam(parts[1]);
                    if (t == null) { sender.sendMessage(color("&cUnknown team.")); return; }
                    store.clearWarpUsageForTeam(t.getID().toString());
                    sender.sendMessage(color("&aCleared warp usage for &e" + t.getName()));
                }
                break;
            case "settag":
                if (!sender.hasPermission(permSetTag)) { sender.sendMessage(color("&cNo permission.")); return; }
                if (parts.length < 3) { sender.sendMessage(color("&cUsage: /btls settag <team> <TAG>")); return; }
                Team t = Team.getTeam(parts[1]);
                if (t == null) { sender.sendMessage(color("&cUnknown team.")); return; }
                String tag = parts[2].toUpperCase(Locale.ROOT);
                if (!tag.matches(tagPattern)) { sender.sendMessage(color("&cInvalid tag format.")); return; }
                try {
                    t.setTag(tag);
                    sender.sendMessage(color("&aSet tag for &e" + t.getName() + " &7to &b" + tag));
                } catch (Exception ex) {
                    sender.sendMessage(color("&cFailed: " + ex.getMessage()));
                }
                break;
            case "setsize":
                if (!sender.hasPermission(permSetSize)) { sender.sendMessage(color("&cNo permission.")); return; }
                if (parts.length < 3) { sender.sendMessage(color("&cUsage: /btls setsize <team> <size>")); return; }
                Team t2 = Team.getTeam(parts[1]);
                if (t2 == null) { sender.sendMessage(color("&cUnknown team.")); return; }
                int size;
                try { size = Integer.parseInt(parts[2]); } catch (NumberFormatException ex) { sender.sendMessage(color("&cInvalid number.")); return; }
                store.setSizeOverride(t2.getID().toString(), size);
                sender.sendMessage(color("&aSet max team size for &e" + t2.getName() + " &7to &b" + size));
                break;
            case "setfreewarps":
                if (!sender.hasPermission(permSetFreeWarps)) { sender.sendMessage(color("&cNo permission.")); return; }
                if (parts.length < 3) { sender.sendMessage(color("&cUsage: /btls setfreewarps <team> <amount>")); return; }
                Team t3 = Team.getTeam(parts[1]);
                if (t3 == null) { sender.sendMessage(color("&cUnknown team.")); return; }
                int amt;
                try { amt = Integer.parseInt(parts[2]); } catch (NumberFormatException ex) { sender.sendMessage(color("&cInvalid number.")); return; }
                store.setFreeWarpsOverride(t3.getID().toString(), amt);
                sender.sendMessage(color("&aSet daily free warps for &e" + t3.getName() + " &7to &b" + amt));
                break;
            default:
                help(sender);
        }
    }

    private void help(CommandSender sender) {
        sender.sendMessage(color("&bTeamsLifesteal Admin"));
        sender.sendMessage(color("&7/btls resetwarpuses [all|<team>]"));
        sender.sendMessage(color("&7/btls settag <team> <TAG>"));
        sender.sendMessage(color("&7/btls setsize <team> <size>"));
        sender.sendMessage(color("&7/btls setfreewarps <team> <amount>"));
    }

    private static String color(String s) { return ChatColor.translateAlternateColorCodes('&', s); }
}
