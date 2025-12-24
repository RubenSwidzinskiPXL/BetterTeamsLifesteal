package com.booksaw.betterTeams.extensions.teamslifesteal.services;

import com.booksaw.betterTeams.Team;
import com.booksaw.betterTeams.TeamPlayer;
import com.booksaw.betterTeams.Main;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class TeamService {
    private final Main plugin;

    public TeamService(Main plugin) {
        this.plugin = plugin;
    }

    public Team getTeam(Player p) {
        return Team.getTeam(p);
    }

    public Team getTeam(OfflinePlayer p) {
        return Team.getTeam(p);
    }

    public boolean sameTeam(Player a, Player b) {
        Team ta = getTeam(a);
        Team tb = getTeam(b);
        return ta != null && ta.equals(tb);
    }

    public String getTag(Team team) {
        if (team == null) return "";
        String tag = team.getTag();
        return tag == null ? "" : tag;
    }

    public List<Team> getOnlineTeams() {
        // Build a set of teams with >=1 online member
        return plugin.getServer().getOnlinePlayers().stream()
                .map(this::getTeam)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Team::getID, t -> t, (a, b) -> a))
                .values().stream().collect(Collectors.toList());
    }

    public int onlineCount(Team team) {
        if (team == null) return 0;
        int c = 0;
        for (TeamPlayer tp : team.getMembers().getClone()) {
            if (tp.getPlayer() != null && tp.getPlayer().isOnline()) c++;
        }
        return c;
    }

    public String getTeamId(Team team) {
        if (team == null) return null;
        return team.getID().toString();
    }

    public UUID getTeamUUID(Team team) {
        if (team == null) return null;
        return team.getID();
    }

    public List<Team> sortByOnlineDesc(List<Team> teams) {
        List<Team> copy = new ArrayList<>(teams);
        copy.sort(Comparator.comparingInt(this::onlineCount).reversed());
        return copy;
    }
}
