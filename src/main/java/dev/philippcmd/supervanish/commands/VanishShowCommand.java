package dev.philippcmd.supervanish.commands;

import dev.philippcmd.supervanish.utils.VanishManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VanishShowCommand implements CommandExecutor {

    private final VanishManager vanishManager;

    public VanishShowCommand(VanishManager vanishManager) {
        this.vanishManager = vanishManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        // Handle arguments
        if (args.length == 0) {
            player.sendMessage("Usage: /vanish-show <player|--all>");
            return true;
        }

        String targetName = args[0];

        // Handle "--all" argument
        if (targetName.equalsIgnoreCase("--all")) {
            int count = 0;
            for (Player vanished : Bukkit.getOnlinePlayers()) {
                if (vanishManager.isVanished(vanished) && !vanishManager.isSuperVanished(vanished)) {
                    vanishManager.addVanishViewer(vanished, player);
                    count++;
                }
            }

            player.sendMessage("You can now see " + count + " vanished players.");
            return true;
        }

        // Handle specific player argument
        Player target = Bukkit.getPlayerExact(targetName);

        if (target == null) {
            player.sendMessage("Player not found: " + targetName);
            return true;
        }

        // Check if the target is vanished
        if (!vanishManager.isVanished(target)) {
            player.sendMessage("Player " + targetName + " is not vanished.");
            return true;
        }

        // Check if the target is in SuperVanish mode
        if (vanishManager.isSuperVanished(target)) {
            player.sendMessage("Player " + targetName + " is in SuperVanish mode and cannot be revealed.");
            return true;
        }

        // Add the player as a vanish viewer
        vanishManager.addVanishViewer(target, player);
        player.sendMessage("You can now see " + target.getName() + ".");
        return true;
    }
}
