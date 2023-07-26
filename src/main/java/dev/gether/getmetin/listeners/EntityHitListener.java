package dev.gether.getmetin.listeners;

import dev.gether.getmetin.GetMetin;
import dev.gether.getmetin.data.MetinData;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityHitListener implements Listener {

    private GetMetin plugin;
    public EntityHitListener(GetMetin plugin)
    {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityHit(EntityDamageByEntityEvent event)
    {
        if(!(event.getDamager() instanceof Player))
            return;

        Entity entity = event.getEntity();
        //boolean isCitizensNPC = entity.hasMetadata("NPC");
//        if(isCitizensNPC)
//            entity.remove();

        MetinData metinData = GetMetin.getInstance().getMetinData().get(entity.getLocation());
        if(metinData==null)
            return;

        Player player = (Player) event.getDamager();
        metinData.hitMetin(player);
    }
}
