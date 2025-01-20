package dev.philippcmd.supervanish.commands;

import dev.philippcmd.supervanish.utils.VanishManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SuperVanishCommand implements CommandExecutor {

    private final VanishManager vanishManager;

    public SuperVanishCommand(VanishManager vanishManager) {
        this.vanishManager = vanishManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        boolean isSuperVanished = vanishManager.toggleSuperVanish(player);

        if (isSuperVanished) {
            player.sendMessage("You are now in SuperVanish mode.");
        } else {
            player.sendMessage("You have left SuperVanish mode.");
        }
        return true;
    }
}
