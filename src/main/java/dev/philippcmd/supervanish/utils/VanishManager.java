package dev.philippcmd.supervanish.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class VanishManager {

    private final Set<Player> vanishedPlayers = new HashSet<>();
    private final Set<Player> superVanishedPlayers = new HashSet<>();

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
        } else {
            vanishedPlayers.add(player);
        }
    }

    public void unvanishPlayer(Player player) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            online.showPlayer(player);
        }
        vanishedPlayers.remove(player);
        superVanishedPlayers.remove(player);
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
}
