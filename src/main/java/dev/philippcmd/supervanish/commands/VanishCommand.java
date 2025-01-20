package dev.philippcmd.supervanish.commands;

import dev.philippcmd.supervanish.utils.VanishManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VanishCommand implements CommandExecutor {

    private final VanishManager vanishManager;

    public VanishCommand(VanishManager vanishManager) {
        this.vanishManager = vanishManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        boolean isVanished = vanishManager.toggleVanish(player);

        if (isVanished) {
            player.sendMessage("You are now invisible.");
        } else {
            player.sendMessage("You are now visible.");
        }
        return true;
    }
}
