## Better Teams

# Introduction:

Create teams to compete to be the best. This plugin is designed to encourage teamwork and foster a sense of community
within a server. BetterTeams includes features such as:

- Teaming up with friends
- Having private chats, unique to each team
- Protecting team members from team-killing.
- Individual homes for each team

[View the wiki for this project](https://booksaw.github.io/BetterTeams/)
[Looking for the Discord Server for support?](https://discord.gg/JF9DNs3)

---

## Teams Lifesteal Extension (Add-on)

An optional BetterTeams extension that adds Lifesteal-safe clan features without modifying BetterTeams core.

What it adds:

- Warp exhaustion with economy fallback (team bank → player via Vault)
- Clan tag policy (regex A-Z0-9, max 5) + LuckPerms meta/prefix
- Sidebar showing only online teams (sorted, capped)
- Friendly-fire protection and teammate heart-steal prevention
- Placeholders: `%teamslf_tag%`, `%teamslf_online%`
- Lightweight admin controls (via `/btls` command interceptor)

Dependencies (Paper 1.21.x):

- BetterTeams (provided)
- Paper API 1.21 (provided)
- Vault (economy)
- Optional: PlaceholderAPI, LuckPerms, TAB, lifestealz

Build/output:

- Module path: `extensions/teams-lifesteal-extension`
- JAR is copied to: `server/plugins/BetterTeams/extensions`

### Configuration

File: `BetterTeams/extensions/teams-lifesteal-extension/config.yml`

- `warp.freePerDay`: free uses per-team per day (overridable per-team)
- `warp.cost`: cost for paid warp when free quota is exhausted
- `warp.payOrder`: `[TEAM, PLAYER]` or `[PLAYER, TEAM]`
- `clanTag.pattern`: enforce `"[A-Z0-9]{1,5}"`
- `sidebar.*`: enabled, title, maxTeams, updateSeconds
- `pvp.*`: blockFriendlyFire, blockSameTeamHeartSteal
- `luckperms.*`: write `team_tag` meta, optional prefix

### Permissions

- `teamslifesteal.admin`: base admin for `/btls`
- `teamslifesteal.admin.resetwarpuses`: use `/btls resetwarpuses [all|<team>]`
- `teamslifesteal.admin.settag`: use `/btls settag <team> <TAG>`
- `teamslifesteal.admin.setsize`: use `/btls setsize <team> <size>`
- `teamslifesteal.admin.setfreewarps`: use `/btls setfreewarps <team> <amount>`
- `teamslifesteal.bypass.warplimit`: bypass daily free warp limit
- `teamslifesteal.bypass.warpcost`: bypass warp cost
- `teamslifesteal.friendlyfire.override`: allow damaging teammates (debug/admin)

### Commands

- `/btls resetwarpuses [all|<team>]`: Reset team warp daily counters
- `/btls settag <team> <TAG>`: Force-set a team tag (validated by regex)
- `/btls setsize <team> <size>`: Override a team-specific member cap (join is blocked if full)
- `/btls setfreewarps <team> <amount>`: Override daily free warp uses for a team

Note: Admin can still use BetterTeams built-ins like `/teama tag`, `/teama setwarp`, etc. The extension does not change core commands.

### Placeholders

- `%teamslf_tag%` → current team tag of the player
- `%teamslf_online%` → online member count of the player's team

### Tutorial (Quick Start)

1) Drop the built JAR into `plugins/BetterTeams/extensions/`
2) Ensure Vault, PlaceholderAPI, LuckPerms, TAB, lifestealz are installed
3) Start server once to generate `config.yml` for the extension
4) Configure:
	- Warp policy (`freePerDay`, `cost`, `payOrder`)
	- Tag policy (`clanTag.pattern`), LuckPerms meta/prefix
	- Sidebar (`title`, `maxTeams`, `updateSeconds`)
	- PVP (`blockFriendlyFire`)
5) Set permissions in LuckPerms, e.g.:
	- `lp user <admin> permission set teamslifesteal.admin true`
	- `lp user <admin> permission set teamslifesteal.admin.resetwarpuses true`
6) Optional admin overrides:
	- `/btls resetwarpuses all`
	- `/btls settag <team> ABC`
	- `/btls setsize <team> 5`
7) Configure TAB/EssentialsXChat to include `%teamslf_tag%` where desired.

### Cross‑Reference (Where features live)

- Warp exhaustion: `TeamWarpListener`, `EconomyService`, `AdminStore`
- Tag rules & LuckPerms meta: `TagEnforcementListener`, `TagMetaListener`
- Sidebar: `SidebarManager`
- Friendly fire: `FriendlyFireListener`
- Placeholders: `TeamPlaceholders`
- Admin controls: `AdminCommandListener`, `JoinLimitListener`
