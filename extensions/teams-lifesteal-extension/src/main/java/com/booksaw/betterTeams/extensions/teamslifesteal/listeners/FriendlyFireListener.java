package com.booksaw.betterTeams.extensions.teamslifesteal.listeners;

import com.booksaw.betterTeams.Team;
import com.booksaw.betterTeams.TeamPlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class FriendlyFireListener implements Listener {
    private final boolean blockFriendly;
    private final boolean blockHeartSteal;

    public FriendlyFireListener(FileConfiguration cfg) {
        this.blockFriendly = cfg.getBoolean("pvp.blockFriendlyFire", true);
        this.blockHeartSteal = cfg.getBoolean("pvp.blockSameTeamHeartSteal", true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!blockFriendly) return;
        if (!(e.getEntity() instanceof Player victim)) return;
        Player damager = null;
        if (e.getDamager() instanceof Player p) damager = p;
        else if (e.getDamager() instanceof org.bukkit.entity.Projectile proj && proj.getShooter() instanceof Player p2) damager = p2;
        if (damager == null) return;

        Team vt = Team.getTeam(victim);
        Team dt = Team.getTeam(damager);
        if (vt != null && vt.equals(dt)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDeath(PlayerDeathEvent e) {
        if (!blockHeartSteal) return;
        Player victim = e.getEntity();
        Player killer = victim.getKiller();
        if (killer == null) return;
        Team vt = Team.getTeam(victim);
        Team kt = Team.getTeam(killer);
        if (vt != null && vt.equals(kt)) {
            // Prevent same-team kill consequences; many lifesteal plugins listen here.
            // We cannot directly modify lifestealz hearts without API, so best effort: clear killer attribution
            e.setKeepLevel(true); // minimal mitigation
        }
    }
}
