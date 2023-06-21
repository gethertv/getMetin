package dev.gether.getmetin.listeners;

import dev.gether.getmetin.GetMetin;
import dev.gether.getmetin.data.MetinData;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlaceBlockListener implements Listener {

    private final GetMetin plugin;

    public PlaceBlockListener(GetMetin plugin)
    {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBreakBlock(BlockPlaceEvent event)
    {
        Player player = event.getPlayer();
        Block block = event.getBlockPlaced();
        MetinData metinData = GetMetin.getInstance().getMetinData().get(block.getLocation());
        if(metinData==null)
            return;

        if(metinData.getMetinLoc().equals(block.getLocation()))
        {
            event.setCancelled(true);
        }

    }
}
