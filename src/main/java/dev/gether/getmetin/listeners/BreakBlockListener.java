package dev.gether.getmetin.listeners;

import dev.gether.getmetin.GetMetin;
import dev.gether.getmetin.data.ItemDrop;
import dev.gether.getmetin.data.MetinData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.Random;

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

            throwPlayer(event.getBlock().getLocation());
            return;
        }

    }

    public static void throwPlayer(Location location) {

        Random random = new Random();
        double chance = GetMetin.getInstance().getConfig().getDouble("push-chance");
        double min = 0;
        double max = 100-chance;
        double startWin = min + (max - min) * random.nextDouble();
        double finishWin = startWin+chance;
        double winTicket = random.nextDouble()*100;

        if(winTicket >= startWin && winTicket <= finishWin) {


            double vectorMultiplier = GetMetin.getInstance().getConfig().getDouble("push-power");

            int sizePush = GetMetin.getInstance().getConfig().getInt("size-push");
            int size = (sizePush/2);
            Location first = location.clone().add(-size, 0, -size);
            Location second = location.clone().add(size, 0, size);
            int top = Math.min(first.getBlockZ(), second.getBlockZ());
            int left = Math.min(first.getBlockX(), second.getBlockX());
            int bottom = Math.max(first.getBlockZ(), second.getBlockZ());
            int right = Math.max(first.getBlockX(), second.getBlockX());

            Collection<Player> nearbyPlayers = location.getNearbyPlayers(sizePush);
            new BukkitRunnable() {

                @Override
                public void run() {
                    for (Player player : nearbyPlayers) {

                        int hitMultiplierX = Math.abs(left - player.getLocation().getX()) > Math.abs(right - player.getLocation().getX()) ? 1 : -1;
                        int hitMultiplierZ = Math.abs(top - player.getLocation().getZ()) > Math.abs(bottom - player.getLocation().getZ()) ? 1 : -1;

                        double meanX = Math.min(Math.abs(left - player.getLocation().getX()), Math.abs(right - player.getLocation().getX()));
                        double meanZ = Math.min(Math.abs(top - player.getLocation().getZ()), Math.abs(bottom - player.getLocation().getZ()));
                        hitMultiplierZ = meanX < meanZ ? 0 : hitMultiplierZ;
                        hitMultiplierX = meanX < meanZ ? hitMultiplierX : 0;

                        Location playerLocation = player.getLocation();
                        Vector vector = playerLocation.getDirection();
                        vector.setX(hitMultiplierX * vectorMultiplier);
                        vector.setY(GetMetin.getInstance().getConfig().getDouble("push-power-y"));
                        vector.setZ(hitMultiplierZ * vectorMultiplier);
                        player.setVelocity(vector);
                    }
                }
            }.runTaskLater(GetMetin.getInstance(), 2L);
        }
    }
}
