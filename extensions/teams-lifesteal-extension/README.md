# Teams Lifesteal Extension

An add-on for BetterTeams that adds Lifesteal-safe team features:

- Warp exhaustion with paid fallback (team bank → player)
- Clan tag rules (A-Z0-9, max 5) + LuckPerms meta/prefix
- Sidebar listing online teams only
- Friendly-fire block and teammate heart-steal prevention
- Placeholders: `%teamslf_tag%`, `%teamslf_online%`

## Install

1. Build the parent project (`mvn -DskipTests package`) using Java 17+.
2. Copy `teams-lifesteal-extension-<version>.jar` to `plugins/BetterTeams/extensions/`.
3. Restart server.

## Configure (`config.yml`)

- `warp.freePerDay`, `warp.cost`, `warp.payOrder` (Paper 1.21.x)
- `clanTag.pattern`, `luckperms.setPrefixToTag`, `luckperms.metaKey`
- `sidebar.enabled`, `sidebar.title`, `sidebar.maxTeams`, `sidebar.updateSeconds`
- `pvp.blockFriendlyFire`, `pvp.blockSameTeamHeartSteal`

## Permissions

- `teamslifesteal.admin` → base for `/btls`
- `teamslifesteal.admin.resetwarpuses`
- `teamslifesteal.admin.settag`
- `teamslifesteal.admin.setsize`
- `teamslifesteal.admin.setfreewarps`
- `teamslifesteal.bypass.warplimit`
- `teamslifesteal.bypass.warpcost`
- `teamslifesteal.friendlyfire.override`

## Commands

- `/btls resetwarpuses [all|<team>]`
- `/btls settag <team> <TAG>`
- `/btls setsize <team> <size>`
- `/btls setfreewarps <team> <amount>`

Note: This command is intercepted via chat; no Bukkit command registration is required.

## Placeholders

- `%teamslf_tag%` — player team tag
- `%teamslf_online%` — online member count

## Files of interest

- Listeners: `TeamWarpListener`, `TagEnforcementListener`, `TagMetaListener`, `FriendlyFireListener`
- Services: `EconomyService`, `TeamService`, `AdminStore`
- UI: `SidebarManager`
- Admin: `AdminCommandListener`, `JoinLimitListener`
