# Teams Lifesteal Extension — New & Overrides

This document focuses only on what this extension adds and what it overrides in BetterTeams.

## New Features

- Warp exhaustion with paid fallback (team bank → player via Vault)
- Clan tag rules (A–Z, 0–9, max 5) + LuckPerms meta/prefix
- Sidebar listing online teams only (sorted, capped)
- Friendly-fire block and same-team heart-steal prevention
- Placeholders: `%teamslf_tag%`, `%teamslf_online%`

## Overrides / Scrapped Core Behavior

- Team warp flow can charge players when daily free quota is exhausted (configurable)
- Tag policy enforced by regex; non‑compliant tags are rejected and sanitized
- LuckPerms meta `team_tag` (and optional prefix) is written by the extension, overriding other tag writers
- Friendly‑fire toggle is enforced regardless of core pvp settings when enabled here

## Install

- Build with `mvn -DskipTests package` (Java 17+)
- Manually place `teams-lifesteal-extension-<version>.jar` into `plugins/BetterTeams/extensions/`
- Restart the server to generate the extension `config.yml`

Note: BetterTeams does not auto‑download or auto‑install extensions. JARs must be placed in `plugins/BetterTeams/extensions/`.

## Commands (Extension‑only)

- `/btls resetwarpuses [all|<team>]`
- `/btls settag <team> <TAG>`
- `/btls setsize <team> <size>`
- `/btls setfreewarps <team> <amount>`

Tab‑completion: These are intercepted via `PlayerCommandPreprocessEvent` and are not registered in Bukkit’s command map, so vanilla tab‑completion will not list them. Commands still work when typed fully.

## Key Configuration

- `warp.freePerDay`, `warp.cost`, `warp.payOrder`
- `clanTag.pattern`, `luckperms.setPrefixToTag`, `luckperms.metaKey`
- `sidebar.enabled`, `sidebar.title`, `sidebar.maxTeams`, `sidebar.updateSeconds`
- `pvp.blockFriendlyFire`, `pvp.blockSameTeamHeartSteal`

## Permissions

- `teamslifesteal.admin`, `teamslifesteal.admin.resetwarpuses`, `teamslifesteal.admin.settag`
- `teamslifesteal.admin.setsize`, `teamslifesteal.admin.setfreewarps`
- `teamslifesteal.bypass.warplimit`, `teamslifesteal.bypass.warpcost`
- `teamslifesteal.friendlyfire.override`
