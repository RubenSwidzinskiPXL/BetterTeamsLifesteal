package com.booksaw.betterTeams.extensions.teamslifesteal.services.storage;

import com.booksaw.betterTeams.extension.BetterTeamsExtension;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AdminStore {
    private final File file;
    private final YamlConfiguration data;

    public AdminStore(BetterTeamsExtension ext) {
        this.file = new File(ext.getDataFolder(), "admin-store.yml");
        this.data = YamlConfiguration.loadConfiguration(file);
    }

    public int getSizeOverride(String teamId, int fallback) {
        return data.getInt("sizeOverrides." + teamId, fallback);
    }

    public void setSizeOverride(String teamId, int size) {
        data.set("sizeOverrides." + teamId, size);
        save();
    }

    public Integer getFreeWarpsOverride(String teamId) {
        if (!data.contains("freeWarpOverrides." + teamId)) return null;
        return data.getInt("freeWarpOverrides." + teamId);
    }

    public void setFreeWarpsOverride(String teamId, int value) {
        data.set("freeWarpOverrides." + teamId, value);
        save();
    }

    public void clearWarpUsageForTeam(String teamId) {
        data.set("warpUsage." + teamId, null);
        save();
    }

    public void clearAllWarpUsage() {
        data.set("warpUsage", null);
        save();
    }

    public int getWarpUsedToday(String teamId, String date) {
        return data.getInt("warpUsage." + teamId + "." + date, 0);
    }

    public void setWarpUsedToday(String teamId, String date, int count) {
        data.set("warpUsage." + teamId + "." + date, count);
        save();
    }

    public void save() {
        try {
            data.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
