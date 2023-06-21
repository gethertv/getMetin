package dev.gether.getmetin.listeners;

import dev.gether.getmetin.GetMetin;
import dev.gether.getmetin.data.MetinData;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakBlockListener implements Listener {

    private final GetMetin plugin;

    public BreakBlockListener(GetMetin plugin)
    {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBreakBlock(BlockBreakEvent event)
    {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        MetinData metinData = GetMetin.getInstance().getMetinData().get(block.getLocation());
        if(metinData==null)
            return;

        if(metinData.getMetin().getMaterial()==block.getType())
        {
            if(metinData.getHp()<=0) return;

            event.setCancelled(true);
            event.setDropItems(false);
            metinData.hitMetin(player);
            return;
        }

    }
}
