package dev.gether.getmetin.listeners;

import dev.gether.getmetin.GetMetin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectListener implements Listener {

    public ConnectListener(GetMetin plugin)
    {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();
        EntityHitListener.cooldown.remove(player.getUniqueId());
    }
}
