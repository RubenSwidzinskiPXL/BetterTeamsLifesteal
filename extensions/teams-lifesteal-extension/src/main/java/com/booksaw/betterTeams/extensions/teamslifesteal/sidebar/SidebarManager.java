package com.booksaw.betterTeams.extensions.teamslifesteal.sidebar;

import com.booksaw.betterTeams.Team;
import com.booksaw.betterTeams.extensions.teamslifesteal.services.TeamService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.ArrayList;
import java.util.List;

public class SidebarManager {
    private final Plugin plugin;
    private final TeamService teams;
    private final String title;
    private final int maxTeams;
    private final long periodTicks;

    private int taskId = -1;

    public SidebarManager(Plugin plugin, TeamService teams, ConfigurationSection cfg) {
        this.plugin = plugin;
        this.teams = teams;
        this.title = color(cfg.getString("title", "&bTeams Online"));
        this.maxTeams = cfg.getInt("maxTeams", 10);
        this.periodTicks = cfg.getLong("updateSeconds", 5) * 20L;
    }

    public void start() {
        if (taskId != -1) return;
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this::updateAll, 40L, periodTicks);
        // Also update on join/quit quickly
        Bukkit.getPluginManager().registerEvents(new SidebarPlayerListener(this), plugin);
    }

    public void stop() {
        if (taskId != -1) {
            Bukkit.getScheduler().cancelTask(taskId);
            taskId = -1;
        }
    }

    private void updateAll() {
        List<Team> online = teams.sortByOnlineDesc(teams.getOnlineTeams());
        if (online.size() > maxTeams) online = new ArrayList<>(online.subList(0, maxTeams));

        List<String> lines = new ArrayList<>();
        for (Team t : online) {
            String tag = teams.getTag(t);
            int c = teams.onlineCount(t);
            lines.add(ChatColor.AQUA + (tag.isEmpty() ? t.getName() : tag) + ChatColor.GRAY + " " + c);
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            applyTo(p, lines);
        }
    }

    public void updateFor(Player p) {
        List<Team> online = teams.sortByOnlineDesc(teams.getOnlineTeams());
        if (online.size() > maxTeams) online = new ArrayList<>(online.subList(0, maxTeams));
        List<String> lines = new ArrayList<>();
        for (Team t : online) {
            String tag = teams.getTag(t);
            int c = teams.onlineCount(t);
            lines.add(ChatColor.AQUA + (tag.isEmpty() ? t.getName() : tag) + ChatColor.GRAY + " " + c);
        }
        applyTo(p, lines);
    }

    private void applyTo(Player p, List<String> lines) {
        ScoreboardManager m = Bukkit.getScoreboardManager();
        if (m == null) return;
        Scoreboard board = m.getNewScoreboard();
        Objective obj = board.registerNewObjective("teamslf", "dummy", title);
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        int score = lines.size();
        for (String line : lines) {
            obj.getScore(line).setScore(score--);
        }
        p.setScoreboard(board);
    }

    private static String color(String s) { return ChatColor.translateAlternateColorCodes('&', s); }
}
