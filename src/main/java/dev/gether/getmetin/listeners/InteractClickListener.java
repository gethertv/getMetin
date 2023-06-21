package dev.gether.getmetin.listeners;

import dev.gether.getmetin.GetMetin;
import dev.gether.getmetin.data.MetinData;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractClickListener implements Listener {

    public InteractClickListener(GetMetin plugin)
    {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler
    public void onInteract(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        if(event.getClickedBlock()==null) return;

        if(event.getAction()!=Action.RIGHT_CLICK_BLOCK)
            return;

        Block block = event.getClickedBlock();
        MetinData metinData = GetMetin.getInstance().getMetinData().get(block.getLocation());
        if(metinData==null)
            return;

        if(metinData.getMetin().getMaterial()==block.getType())
        {
            event.setCancelled(true);
        }
    }
}
