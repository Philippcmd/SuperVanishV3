package dev.philippcmd.supervanish.utils;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class VanishManager {

    private final JavaPlugin plugin;

    // Runtime sets — online players only
    private final Set<Player> vanishedPlayers = new HashSet<>();
    private final Set<Player> superVanishedPlayers = new HashSet<>();
    private final HashMap<Player, Set<Player>> vanishViewers = new HashMap<>();

    // Persistent sets — UUIDs, includes offline players
    private final Set<UUID> persistentVanished = new HashSet<>();
    private final Set<UUID> persistentSuperVanished = new HashSet<>();

    private final File dataFile;
    private final FileConfiguration dataConfig;

    public VanishManager(JavaPlugin plugin) {
        this.plugin = plugin;
        dataFile = new File(plugin.getDataFolder(), "players.yml");
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        loadData();
    }

    private void loadData() {
        for (String s : dataConfig.getStringList("vanished")) {
            try { persistentVanished.add(UUID.fromString(s)); } catch (IllegalArgumentException ignored) {}
        }
        for (String s : dataConfig.getStringList("super-vanished")) {
            try { persistentSuperVanished.add(UUID.fromString(s)); } catch (IllegalArgumentException ignored) {}
        }
    }

    private void saveData() {
        dataConfig.set("vanished", persistentVanished.stream().map(UUID::toString).collect(Collectors.toList()));
        dataConfig.set("super-vanished", persistentSuperVanished.stream().map(UUID::toString).collect(Collectors.toList()));
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to save players.yml: " + e.getMessage());
        }
    }

    public void restoreVanishOnJoin(Player player) {
        UUID uuid = player.getUniqueId();
        if (persistentSuperVanished.contains(uuid)) {
            vanishPlayer(player, true);
        } else if (persistentVanished.contains(uuid)) {
            vanishPlayer(player, false);
        }
    }

    public void handlePlayerQuit(Player player) {
        // Remove from runtime sets without touching the file — UUID sets remain intact
        vanishedPlayers.remove(player);
        superVanishedPlayers.remove(player);
        vanishViewers.remove(player);
    }

    public boolean toggleVanish(Player player) {
        if (vanishedPlayers.contains(player)) {
            unvanishPlayer(player);
            return false;
        } else {
            vanishPlayer(player, false);
            return true;
        }
    }

    public boolean toggleSuperVanish(Player player) {
        if (superVanishedPlayers.contains(player)) {
            unvanishPlayer(player);
            return false;
        } else {
            vanishPlayer(player, true);
            return true;
        }
    }

    public void vanishPlayer(Player player, boolean superVanish) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            online.hidePlayer(player);
        }

        if (superVanish) {
            superVanishedPlayers.add(player);
            persistentSuperVanished.add(player.getUniqueId());
            persistentVanished.remove(player.getUniqueId());
        } else {
            vanishedPlayers.add(player);
            persistentVanished.add(player.getUniqueId());
            persistentSuperVanished.remove(player.getUniqueId());
        }
        vanishViewers.put(player, new HashSet<>());
        saveData();
    }

    public void unvanishPlayer(Player player) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            online.showPlayer(player);
        }
        vanishedPlayers.remove(player);
        superVanishedPlayers.remove(player);
        vanishViewers.remove(player);
        persistentVanished.remove(player.getUniqueId());
        persistentSuperVanished.remove(player.getUniqueId());
        saveData();
    }

    public void showVanishedPlayers(Player player) {
        for (Player vanished : vanishedPlayers) {
            player.showPlayer(vanished);
        }
    }

    public List<String> getVanishedPlayerNames() {
        return vanishedPlayers.stream()
                .map(Player::getName)
                .collect(Collectors.toList());
    }

    public boolean isVanished(Player player) {
        return vanishedPlayers.contains(player) || superVanishedPlayers.contains(player);
    }

    public boolean isSuperVanished(Player player) {
        return superVanishedPlayers.contains(player);
    }

    public void addVanishViewer(Player vanished, Player viewer) {
        if (!isVanished(vanished)) return; // Ignore if the player is not vanished
        if (!vanishViewers.containsKey(vanished)) {
            vanishViewers.put(vanished, new HashSet<>());
        }

        // Add the viewer to the vanishViewers map
        vanishViewers.get(vanished).add(viewer);
        viewer.showPlayer(vanished);
    }
}
