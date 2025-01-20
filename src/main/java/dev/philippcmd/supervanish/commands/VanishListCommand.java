package dev.philippcmd.supervanish.commands;

import dev.philippcmd.supervanish.utils.VanishManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class VanishListCommand implements CommandExecutor {

    private final VanishManager vanishManager;

    public VanishListCommand(VanishManager vanishManager) {
        this.vanishManager = vanishManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        List<String> vanishedPlayers = vanishManager.getVanishedPlayerNames();

        if (vanishedPlayers.isEmpty()) {
            sender.sendMessage("No players are currently vanished.");
        } else {
            sender.sendMessage("Vanished players: " + String.join(", ", vanishedPlayers));
        }
        return true;
    }
}
