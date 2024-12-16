package dev.philippcmd.supervanish;

        import org.bukkit.Bukkit;
        import org.bukkit.ChatColor;
        import org.bukkit.command.Command;
        import org.bukkit.command.CommandSender;
        import org.bukkit.entity.Player;
        import org.bukkit.plugin.java.JavaPlugin;

        import java.util.HashSet;
        import java.util.Set;

public class Main extends JavaPlugin {

    private final Set<Player> vanishedPlayers = new HashSet<>();
    private final Set<Player> superVanishedPlayers = new HashSet<>();

    @Override
    public void onEnable() {
        getLogger().info("SuperVanish Plugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("SuperVanish Plugin disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        switch (command.getName().toLowerCase()) {
            case "vanish":
                toggleVanish(player);
                return true;

            case "vanish-show":
                showVanishedPlayers(player);
                return true;

            case "supervanish":
                toggleSuperVanish(player);
                return true;

            default:
                return false;
        }
    }

    private void toggleVanish(Player player) {
        if (superVanishedPlayers.contains(player)) {
            player.sendMessage(ChatColor.RED + "You cannot toggle vanish while in SuperVanish mode.");
            return;
        }

        if (vanishedPlayers.contains(player)) {
            vanishedPlayers.remove(player);
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.showPlayer(this, player);
            }
            player.sendMessage(ChatColor.GREEN + "You are now visible.");
        } else {
            vanishedPlayers.add(player);
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.hidePlayer(this, player);
            }
            player.sendMessage(ChatColor.GREEN + "You are now invisible.");
        }
    }

    private void showVanishedPlayers(Player player) {
        player.sendMessage(ChatColor.YELLOW + "Vanished players:");
        for (Player vanished : vanishedPlayers) {
            if (!superVanishedPlayers.contains(vanished)) {
                player.sendMessage(ChatColor.GRAY + "- " + vanished.getName());
            }
        }
    }

    private void toggleSuperVanish(Player player) {
        if (superVanishedPlayers.contains(player)) {
            superVanishedPlayers.remove(player);
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.showPlayer(this, player);
            }
            player.sendMessage(ChatColor.GREEN + "SuperVanish mode disabled. You are now visible.");
        } else {
            superVanishedPlayers.add(player);
            vanishedPlayers.remove(player); // Ensure no conflict with regular vanish
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.hidePlayer(this, player);
            }
            player.sendMessage(ChatColor.GREEN + "SuperVanish mode enabled. You are completely invisible.");
        }
    }
}
