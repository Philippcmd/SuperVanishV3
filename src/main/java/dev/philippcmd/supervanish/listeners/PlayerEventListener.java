package dev.philippcmd.supervanish.listeners;

import dev.philippcmd.supervanish.utils.VanishManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEventListener implements Listener {

    private final VanishManager vanishManager;

    public PlayerEventListener(VanishManager vanishManager) {
        this.vanishManager = vanishManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        vanishManager.restoreVanishOnJoin(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        vanishManager.handlePlayerQuit(event.getPlayer());
    }
}
