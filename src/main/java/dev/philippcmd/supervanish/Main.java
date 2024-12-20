package dev.philippcmd.supervanish;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {

    private final List<Player> vanishedPlayers = new ArrayList<>();
    private final List<Player> superVanishedPlayers = new ArrayList<>();

    @Override
    public void onEnable() {
        getLogger().info("SuperVanish Plugin enabled!");
    }

    @Override
    public void onDisable() {
        // Ensure all players are made visible when the plugin is disabled.
        for (Player player : vanishedPlayers) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.showPlayer(this, player);
            }
        }
        for (Player player : superVanishedPlayers) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.showPlayer(this, player);
            }
        }
        getLogger().info("SuperVanish Plugin disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;

        switch (command.getName().toLowerCase()) {
            case "vanish":
                handleVanishCommand(player);
                break;

            case "vanish-show":
                handleVanishShowCommand(player, args);
                break;

            case "vanish-list":
                handleVanishListCommand(player);
                break;

            case "supervanish":
                handleSuperVanishCommand(player);
                break;

            default:
                return false;
        }
        return true;
    }

    private void handleVanishCommand(Player player) {
        if (superVanishedPlayers.contains(player)) {
            player.sendMessage(ChatColor.RED + "You cannot use /vanish while in Super-Vanish mode.");
            return;
        }

        if (vanishedPlayers.contains(player)) {
            vanishedPlayers.remove(player);
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.showPlayer(this, player);
            }
            player.sendMessage(ChatColor.GREEN + "You are now visible.");
        } else {
            vanishedPlayers.add(player);
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!p.equals(player)) {
                    p.hidePlayer(this, player);
                }
            }
            player.sendMessage(ChatColor.YELLOW + "You are now vanished.");
        }
    }

    private void handleVanishShowCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /vanish-show <player>");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !vanishedPlayers.contains(target)) {
            player.sendMessage(ChatColor.RED + "Player not found or not in vanish.");
            return;
        }

        player.showPlayer(this, target);
        player.sendMessage(ChatColor.GREEN + target.getName() + " is now visible to you.");
    }

    private void handleVanishListCommand(Player player) {
        if (!vanishedPlayers.isEmpty()) {
            player.sendMessage(ChatColor.YELLOW + "Vanished players:");
            vanishedPlayers.stream()
                    .filter(p -> !superVanishedPlayers.contains(p))
                    .forEach(p -> player.sendMessage(ChatColor.GRAY + "- " + p.getName()));
        } else {
            player.sendMessage(ChatColor.RED + "No players are currently in vanish.");
        }
    }

    private void handleSuperVanishCommand(Player player) {
        if (superVanishedPlayers.contains(player)) {
            superVanishedPlayers.remove(player);
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.showPlayer(this, player);
            }
            player.sendMessage(ChatColor.GREEN + "Super-Vanish disabled. You are now visible.");
        } else {
            superVanishedPlayers.add(player);
            vanishedPlayers.remove(player);
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.hidePlayer(this, player);
            }
            player.sendMessage(ChatColor.DARK_PURPLE + "Super-Vanish enabled. You are completely invisible.");
        }
    }
}
