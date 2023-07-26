package dev.gether.getmetin.listeners;

import dev.gether.getmetin.GetMetin;
import dev.gether.getmetin.data.MetinData;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.HashMap;
import java.util.UUID;

public class EntityHitListener implements Listener {

    private GetMetin plugin;
    public EntityHitListener(GetMetin plugin)
    {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public static HashMap<UUID, Long> cooldown = new HashMap<>();

    @EventHandler
    public void onEntityHit(EntityDamageByEntityEvent event)
    {
        if(!(event.getDamager() instanceof Player))
            return;

        Entity entity = event.getEntity();
        //boolean isCitizensNPC = entity.hasMetadata("NPC");
//        if(isCitizensNPC)
//            entity.remove();

        Location location1 = entity.getLocation().getBlock().getLocation().clone().add(0, 1, 0);
        Location location2 = entity.getLocation().getBlock().getLocation();

        MetinData metinData1 = GetMetin.getInstance().getMetinData().get(location1);
        MetinData metinData2 = GetMetin.getInstance().getMetinData().get(location2);
        Player player = (Player) event.getDamager();
        Long aLong = cooldown.get(player.getUniqueId());
        if(aLong!=null && aLong>=System.currentTimeMillis())
            return;

        if(metinData1!=null)
        {
            metinData1.hitMetin(player);
            cooldown.put(player.getUniqueId(), System.currentTimeMillis()+1000L);
            return;
        }
        if(metinData2!=null)
        {
            metinData2.hitMetin(player);
            cooldown.put(player.getUniqueId(), System.currentTimeMillis()+1000L);
            return;
        }

    }
}
