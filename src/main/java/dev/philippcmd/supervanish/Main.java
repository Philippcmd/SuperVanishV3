package dev.philippcmd.supervanish;

import dev.philippcmd.supervanish.commands.VanishCommand;
import dev.philippcmd.supervanish.commands.SuperVanishCommand;
import dev.philippcmd.supervanish.commands.VanishShowCommand;
import dev.philippcmd.supervanish.commands.VanishListCommand;
import dev.philippcmd.supervanish.listeners.PlayerEventListener;
import dev.philippcmd.supervanish.tabcompleters.VanishShowTabCompleter;
import dev.philippcmd.supervanish.utils.VanishManager;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static VanishManager vanishManager;

    @Override
    public void onEnable() {
        vanishManager = new VanishManager(this);

        getCommand("vanish").setExecutor(new VanishCommand(vanishManager));
        getCommand("supervanish").setExecutor(new SuperVanishCommand(vanishManager));
        getCommand("vanish-show").setExecutor(new VanishShowCommand(vanishManager));
        getCommand("vanish-list").setExecutor(new VanishListCommand(vanishManager));

        getCommand("vanish-show").setTabCompleter(new VanishShowTabCompleter());

        getServer().getPluginManager().registerEvents(new PlayerEventListener(vanishManager), this);

        getLogger().info("SuperVanish Plugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("SuperVanish Plugin disabled!");
    }

    public static VanishManager getVanishManager() {
        return vanishManager;
    }
}
