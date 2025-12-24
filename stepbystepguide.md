🧠 Goal Recap (What We’re Building)

A team/clan system that:

Uses BetterTeams as the core

Adds:

Team tags (max 5 chars)

Team size / homes / warps set by admin

Shared + individual economy logic

Warp exhaustion → paid warps

Sidebar showing only online teams

Chat, TAB, and nametag integration

Is Lifesteal-safe

Does not sacrifice performance

📦 Your Current Plugin Stack (Relevant)
Core / Required (Already Perfect)

✅ BetterTeams

✅ Vault

✅ EssentialsX + EssentialsXChat

✅ LuckPerms

✅ PlaceholderAPI + PAPIProxyBridge

✅ TAB

✅ ProtocolLib

✅ lifestealz

UI / Utility You Can Leverage

zMenu (GUI)

Skript (optional prototyping)

DiscordSRV (later hooks)

PvPManager (friendly-fire logic)

⚠️ No conflicts here. Good foundation.

🏗 Architecture Decision (IMPORTANT)
❌ Don’t modify BetterTeams directly

You will lose changes on update

Harder to debug

Harder to collaborate

✅ Create a new plugin

Let’s call it:

TeamsLifestealExtension

This plugin:

Depends on BetterTeams

Reads / modifies teams via the API

Adds economy + UI logic

Controls Lifesteal-specific rules

🧩 Phase 1 — Prepare BetterTeams (Configuration)
Step 1.1 — Lock core settings

In BetterTeams/config.yml:

Disable features you don’t want duplicated:

Chat formatting (if TAB handles it)

Unused team commands

Set default max team size (admin adjustable later)

Enable:

Team homes

Team warps

Team bank

This gives you:
✔ Stable storage
✔ Permissions
✔ Team persistence

Step 1.2 — Permissions sanity

Use LuckPerms to:

Limit who can:

Create teams

Rename tags

Invite players

Prepare admin nodes:

teams.admin.setsize

teams.admin.setwarps

teams.admin.sethomes

🧱 Phase 2 — Create Your Extension Plugin
Step 2.1 — Project setup

Java plugin

Depends on:

BetterTeams

Vault

PlaceholderAPI

TAB (soft-depend)

plugin.yml

depend:
  - BetterTeams
  - Vault
softdepend:
  - PlaceholderAPI
  - TAB

Step 2.2 — Core services layer

Create service wrappers:

TeamService (wraps BetterTeams API)

EconomyService (Vault abstraction)

ConfigService

This prevents API lock-in later.

💰 Phase 3 — Economy Logic (Shared + Individual)
Step 3.1 — Vault integration

Use:

Team balance (BetterTeams)

Player balance (Vault)

Step 3.2 — Warp exhaustion system

Logic flow:

Each team has:

freeWarpsPerDay

On warp:

If free warps remain → allow

Else:

Try team bank

Else try player balance

Else deny

Persist:

Daily counters (reset on date change)

Storage:

YAML now

SQLite later (recommended)

Step 3.3 — Home logic (optional)

Free homes up to config limit

Extra homes:

Cost increases per home

Charged to team or player

🏷 Phase 4 — Clan Tags (Chat, TAB, Nametag)
Step 4.1 — Tag rules

Max 5 characters

Regex enforced:

[A-Z0-9]{1,5}

Step 4.2 — Where tags appear

Chat → EssentialsXChat

TAB → TAB plugin

Nametag → TAB / ProtocolLib

Step 4.3 — Implementation

On join / team change:

Set LuckPerms meta:

prefix=[TAG]

Register PlaceholderAPI placeholders:

%team_tag%

%team_online%

TAB config handles rendering — no duplication.

📊 Phase 5 — Sidebar (Online Teams Only)
Step 5.1 — Data rules

Sidebar shows:

Teams Online
────────────
ABC 3/5
XYZ 1/4


Rules:

Only teams with ≥1 online member

Configurable max teams displayed

Sorted by online count (desc)

Step 5.2 — Technical approach

Use:

Bukkit Scoreboard API or

FastBoard-style async updates

Update triggers:

Player join/quit

Team join/leave

Every 5 seconds (fallback)

Avoid:
❌ Per-tick updates
❌ Per-player recalculations

❤️ Phase 6 — Lifesteal Safety Rules
Step 6.1 — Friendly fire logic

Decide:

Teammates:

Can damage? (recommended: ❌)

Can steal hearts? (❌)

Implement:

EntityDamageByEntityEvent

PlayerDeathEvent

Override lifestealz behavior only for teammates

Step 6.2 — Exploit prevention

Block:

Alt farming within teams

Repeated kill loops

Same-IP kill farming (optional)

🧑‍💼 Phase 7 — Admin Controls

Commands:

/team admin setsize <team> <size>

/team admin setwarps <team> <amount>

/team admin resetwarpuses

/team admin settag <team> <tag>

Permissions via LuckPerms.

🌐 Phase 8 — Velocity Compatibility

You already have:

PAPIProxyBridge

TAB

Ensure:

Team data stored server-side

Placeholders resolve proxy-wide

No hard dependency on local scoreboard state

Optional later:

Redis / MySQL for cross-server sync

🚀 Phase 9 — Testing & Rollout
Test cases

Team creation/deletion

Warp exhaustion

Economy fallback

Lifesteal kills

Sidebar refresh load

Tag updates live

Performance checks

Profile scoreboard updates

Monitor async tasks

Ensure no sync DB calls

✅ Final Recommendation

Do exactly this:

Use BetterTeams as the base

Build TeamsLifestealExtension

Integrate via:

Vault

LuckPerms

TAB

PlaceholderAPI

You get:

Stability

Security

Performance

Upgrade safety