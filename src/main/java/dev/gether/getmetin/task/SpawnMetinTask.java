package dev.gether.getmetin.task;

import dev.gether.getmetin.GetMetin;
import dev.gether.getmetin.data.MetinData;
import dev.gether.getmetin.metin.Metin;
import dev.gether.getmetin.utils.ColorFixer;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class SpawnMetinTask extends BukkitRunnable {

    private final GetMetin plugin;
    public SpawnMetinTask(GetMetin plugin)
    {
        this.plugin = plugin;
    }
    @Override
    public void run() {
        for(MetinData metinData : plugin.getMetinData().values())
        {
            long nowTime = System.currentTimeMillis();
            if(metinData.getSpawnLong()>=nowTime)
            {
                int second = (int) (metinData.getSpawnLong() - nowTime)/1000;
                if(plugin.getConfig().isSet("broadcast."+second))
                {
                    Bukkit.broadcastMessage(ColorFixer.addColors(
                            plugin.getConfig().getString("broadcast."+second)
                                    .replace("{name}", metinData.getMetin().getName())
                                    .replace("{x}",String.valueOf( metinData.getMetinLoc().getBlockX()))
                                    .replace("{y}",String.valueOf( metinData.getMetinLoc().getBlockY()))
                                    .replace("{z}",String.valueOf( metinData.getMetinLoc().getBlockZ()))
                    ));
                }

                continue;
            }

            metinData.createMetin();
        }
    }
}
