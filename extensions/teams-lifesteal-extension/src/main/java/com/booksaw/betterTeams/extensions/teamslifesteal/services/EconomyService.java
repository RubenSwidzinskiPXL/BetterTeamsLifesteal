package com.booksaw.betterTeams.extensions.teamslifesteal.services;

import com.booksaw.betterTeams.Main;
import com.booksaw.betterTeams.Team;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class EconomyService {
    private final Main plugin;
    private Economy econ;

    public EconomyService(Main plugin) {
        this.plugin = plugin;
        if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            this.econ = plugin.getServer().getServicesManager().getRegistration(Economy.class) != null
                    ? plugin.getServer().getServicesManager().getRegistration(Economy.class).getProvider()
                    : null;
        }
    }

    public boolean hasEconomy() { return econ != null; }

    public double getBalance(Player p) {
        if (econ == null) return 0.0;
        return econ.getBalance(p);
    }

    public boolean withdraw(Player p, double amount) {
        if (econ == null) return false;
        return econ.withdrawPlayer((OfflinePlayer)p, amount).transactionSuccess();
    }

    // Team bank is handled by BetterTeams directly. Provide simple helpers.
    public double getTeamBalance(Team team) {
        if (team == null) return 0.0;
        return team.getMoney();
    }

    public boolean withdrawTeam(Team team, double amount) {
        if (team == null) return false;
        if (team.getMoney() < amount) return false;
        team.setMoney(team.getMoney() - amount);
        return true;
    }
}
