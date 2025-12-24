---
id: teams-lifesteal
title: Teams Lifesteal Extension
sidebar_label: Teams Lifesteal
---

Adds Lifesteal-safe features to BetterTeams via an extension:

- Warp exhaustion with paid fallback (team bank → player)
- Clan tag rules (A-Z0-9 up to 5) + LuckPerms meta/prefix
- Sidebar of online teams only
- Prevent friendly-fire and teammate heart stealing
- Placeholders: `%teamslf_tag%`, `%teamslf_online%`

## Install

- Build with Maven and copy the JAR to `plugins/BetterTeams/extensions/`.
- Start server and edit the generated `config.yml`.

## Configure

See `BetterTeams/extensions/teams-lifesteal-extension/config.yml` for:

- Warp: `freePerDay`, `cost`, `payOrder`, daily counters
- Tags: `clanTag.pattern` (`[A-Z0-9]{1,5}`), LuckPerms meta/prefix
- Sidebar: `title`, `maxTeams`, `updateSeconds`
- PvP: `blockFriendlyFire`, `blockSameTeamHeartSteal`

## Permissions

```
teamslifesteal.admin
teamslifesteal.admin.resetwarpuses
teamslifesteal.admin.settag
teamslifesteal.admin.setsize
teamslifesteal.admin.setfreewarps
teamslifesteal.bypass.warplimit
teamslifesteal.bypass.warpcost
teamslifesteal.friendlyfire.override
```

## Commands

```
/btls resetwarpuses [all|<team>]
/btls settag <team> <TAG>
/btls setsize <team> <size>
/btls setfreewarps <team> <amount>
```

## Placeholders

- `%teamslf_tag%` → team tag for the player
- `%teamslf_online%` → online member count for the player's team

## Notes

- Uses BetterTeams APIs only; no core modifications.
- Admin can still use `/teama` commands provided by BetterTeams.
