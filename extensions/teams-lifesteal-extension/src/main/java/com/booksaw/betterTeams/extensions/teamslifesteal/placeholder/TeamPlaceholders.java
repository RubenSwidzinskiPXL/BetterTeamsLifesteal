package com.booksaw.betterTeams.extensions.teamslifesteal.placeholder;

import com.booksaw.betterTeams.Team;
import com.booksaw.betterTeams.extensions.teamslifesteal.services.TeamService;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class TeamPlaceholders extends PlaceholderExpansion {

    private final TeamService teams;
    private final String identifier;

    public TeamPlaceholders(TeamService teams, ConfigurationSection cfg) {
        this.teams = teams;
        this.identifier = cfg != null ? cfg.getString("identifier", "teamslf") : "teamslf";
    }

    @Override
    public @NotNull String getIdentifier() {
        return identifier;
    }

    @Override
    public @NotNull String getAuthor() {
        return "BetterTeams";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) return "";
        Team t = teams.getTeam(player);
        switch (params.toLowerCase()) {
            case "tag":
                return teams.getTag(t);
            case "online":
            case "online_count":
                return Integer.toString(teams.onlineCount(t));
            default:
                return null;
        }
    }
}
