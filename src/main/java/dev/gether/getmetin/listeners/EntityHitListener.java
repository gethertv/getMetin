package dev.gether.getmetin.listeners;

import dev.gether.getmetin.GetMetin;
import dev.gether.getmetin.data.MetinData;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.UUID;

public class EntityHitListener implements Listener {

    public EntityHitListener(GetMetin plugin)
    {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private final static HashMap<UUID, Long> cooldown = new HashMap<>();

    @EventHandler
    public void onEntityHit(EntityDamageByEntityEvent event)
    {
        if(!(event.getDamager() instanceof Player))
            return;

        Entity entity = event.getEntity();
        Player player = (Player) event.getDamager();
        boolean isCitizensNPC = entity.hasMetadata("NPC");
        if(!isCitizensNPC) {
            return;
        }

        Location location = entity.getLocation().getBlock().getLocation();

        MetinData metinFirstCheck = GetMetin.getInstance().getMetinData().get(location.clone().add(0,1,0));
        MetinData metinSecondCheck = GetMetin.getInstance().getMetinData().get(location);

        Long aLong = cooldown.get(player.getUniqueId());
        if(aLong!=null && aLong>=System.currentTimeMillis())
            return;

        if(metinFirstCheck!=null)
        {
            metinFirstCheck.hitMetin(player);
            cooldown.put(player.getUniqueId(), System.currentTimeMillis()+1000L);
            return;
        }
        if(metinSecondCheck!=null)
        {
            metinSecondCheck.hitMetin(player);
            cooldown.put(player.getUniqueId(), System.currentTimeMillis()+1000L);
            return;
        }

    }

    public static HashMap<UUID, Long> getCooldown() {
        return cooldown;
    }
}
