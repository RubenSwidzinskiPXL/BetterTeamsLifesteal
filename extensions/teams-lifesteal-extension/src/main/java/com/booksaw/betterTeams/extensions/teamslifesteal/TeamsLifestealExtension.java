package com.booksaw.betterTeams.extensions.teamslifesteal;

import com.booksaw.betterTeams.extension.BetterTeamsExtension;
import com.booksaw.betterTeams.extensions.teamslifesteal.listeners.FriendlyFireListener;
import com.booksaw.betterTeams.extensions.teamslifesteal.listeners.TagEnforcementListener;
import com.booksaw.betterTeams.extensions.teamslifesteal.listeners.TeamWarpListener;
import com.booksaw.betterTeams.extensions.teamslifesteal.listeners.AdminCommandListener;
import com.booksaw.betterTeams.extensions.teamslifesteal.listeners.JoinLimitListener;
import com.booksaw.betterTeams.extensions.teamslifesteal.listeners.TagMetaListener;
import com.booksaw.betterTeams.extensions.teamslifesteal.placeholder.TeamPlaceholders;
import com.booksaw.betterTeams.extensions.teamslifesteal.services.EconomyService;
import com.booksaw.betterTeams.extensions.teamslifesteal.services.TeamService;
import com.booksaw.betterTeams.extensions.teamslifesteal.sidebar.SidebarManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;

public class TeamsLifestealExtension extends BetterTeamsExtension {

    private EconomyService economyService;
    private TeamService teamService;
    private SidebarManager sidebarManager;

    @Override
    public void onEnable() {
        try {
            // Ensure default config
            saveResource("config.yml", false);
            getConfig();

            FileConfiguration cfg = getConfig().config;

            // Services
            this.teamService = new TeamService(getPlugin());
            this.economyService = new EconomyService(getPlugin());

            // Placeholders (soft)
            if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                new TeamPlaceholders(teamService, cfg.getConfigurationSection("placeholders"))
                        .register();
            }

            // Listeners
            Bukkit.getPluginManager().registerEvents(new TagEnforcementListener(cfg), getPlugin());
            Bukkit.getPluginManager().registerEvents(new FriendlyFireListener(cfg), getPlugin());
            Bukkit.getPluginManager().registerEvents(new TeamWarpListener(cfg, teamService, economyService, this), getPlugin());
            Bukkit.getPluginManager().registerEvents(new AdminCommandListener(this, cfg, teamService), getPlugin());
            Bukkit.getPluginManager().registerEvents(new JoinLimitListener(new com.booksaw.betterTeams.extensions.teamslifesteal.services.storage.AdminStore(this)), getPlugin());
            Bukkit.getPluginManager().registerEvents(new TagMetaListener(cfg), getPlugin());

            // Sidebar
            if (cfg.getBoolean("sidebar.enabled", true)) {
                this.sidebarManager = new SidebarManager(getPlugin(), teamService, cfg.getConfigurationSection("sidebar"));
                this.sidebarManager.start();
            }

            getLogger().info("TeamsLifestealExtension enabled");
        } catch (Exception e) {
            getLogger().warning("Failed to enable TeamsLifestealExtension: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(getPlugin());
        if (sidebarManager != null) {
            sidebarManager.stop();
            sidebarManager = null;
        }
    }
}
