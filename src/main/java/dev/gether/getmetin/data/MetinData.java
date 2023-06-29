package dev.gether.getmetin.data;

import dev.gether.getmetin.GetMetin;
import dev.gether.getmetin.metin.Metin;
import dev.gether.getmetin.utils.ColorFixer;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;

public class MetinData {

    private String key;
    private Hologram hologram;
    private int hp;
    private Location metinLoc;
    private Metin metin;

    private Long spawnLong;
    private int second;
    public MetinData(String key, Location metinLoc, Metin metin, int second) {
        this.key = key;
        this.metinLoc = metinLoc;
        this.hp = 0;
        this.second = second;
        this.metin = metin;
        spawnLong = System.currentTimeMillis()+(second*1000L);
    }

    public void createMetin() {
        if(this.hp<=0 || metinLoc.getBlock().getType()!=metin.getMaterial()) {
            this.hp = metin.getMaxHp();
            spawnLong = System.currentTimeMillis() + (second * 1000L);
            metinLoc.getBlock().setType(metin.getMaterial());
            hologram = DHAPI.createHologram(UUID.randomUUID().toString(), metinLoc.clone().add(0.5, GetMetin.getInstance().getConfig().getDouble("hologram-y"), 0.5), getHolo());
            return;
        }
        this.hp = metin.getMaxHp();
        updateHolo();
        spawnLong = System.currentTimeMillis() + (second * 1000L);
    }

    public List<String> getHolo()
    {
        List<String> hologramLine = new ArrayList<>();
        for (int i = 0; i < metin.getHologramLine().size(); i++) {
            hologramLine.add(
                    metin.getHologramLine().get(i).replace("{actually-hp}", String.valueOf(hp))
                                                    .replace("{max-hp}", String.valueOf(metin.getMaxHp()))
            );
        }
        return hologramLine;
    }

    public void updateHolo()
    {
        DHAPI.setHologramLines(hologram, getHolo());
    }
    public void hitMetin(Player player)
    {
        hp--;
        if(hp<=0)
        {
            giveFinalReward(player, metinLoc);
            Bukkit.broadcastMessage(ColorFixer.addColors(GetMetin.getInstance().getConfig().getString("lang.metin-down")
                    .replace("{x}", String.valueOf(metinLoc.getBlockX()))
                    .replace("{y}", String.valueOf(metinLoc.getBlockY()))
                    .replace("{z}", String.valueOf(metinLoc.getBlockZ()))
            ));
            hologram.destroy();
            metinLoc.getBlock().setType(Material.AIR);
            return;
        }
        giveReward(player, metinLoc);
        updateHolo();

    }

    public Metin getMetin() {
        return metin;
    }

    public Hologram getHologram() {
        return hologram;
    }

    public Location getMetinLoc() {
        return metinLoc;
    }

    public int getHp() {
        return hp;
    }

    private void giveFinalReward(Player player, Location location) {
        Random random = new Random();
        for (ItemDrop item : metin.getFinalRewards().getItems()) {
            double min = 0;
            double max = 100-item.getChance();
            double startWin = min + (max - min) * random.nextDouble();
            double finishWin = startWin+item.getChance();
            double winTicket = random.nextDouble()*100;

            if(winTicket >= startWin && winTicket <= finishWin)
            {
                if(!isInventoryFull(player))
                    player.getInventory().addItem(item.getItemStack());
                else
                    location.getBlock().getWorld().dropItemNaturally(location, item.getItemStack());
            }
        }
        for (CmdDrop cmdData : metin.getFinalRewards().getCommands()) {
            double min = 0;
            double max = 100-cmdData.getChance();
            double startWin = min + (max - min) * random.nextDouble();
            double finishWin = startWin+cmdData.getChance();
            double winTicket = random.nextDouble()*100;

            if(winTicket >= startWin && winTicket <= finishWin)
            {
               Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmdData.getCommand().replace("{player}", player.getName()));
            }
        }
    }

    private void giveReward(Player player, Location location) {
        Random random = new Random();
        for (ItemDrop item : metin.getDropItems()) {
            double min = 0;
            double max = 100-item.getChance();
            double startWin = min + (max - min) * random.nextDouble();
            double finishWin = startWin+item.getChance();
            double winTicket = random.nextDouble()*100;

            if(winTicket >= startWin && winTicket <= finishWin)
            {
                if(!isInventoryFull(player))
                    player.getInventory().addItem(item.getItemStack());
                else
                    location.getBlock().getWorld().dropItemNaturally(location, item.getItemStack());
            }
        }
        for (CmdDrop cmdData : metin.getDropCmds()) {
            double min = 0;
            double max = 100-cmdData.getChance();
            double startWin = min + (max - min) * random.nextDouble();
            double finishWin = startWin+cmdData.getChance();
            double winTicket = random.nextDouble()*100;

            if(winTicket >= startWin && winTicket <= finishWin)
            {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmdData.getCommand().replace("{player}", player.getName()));
            }
        }
    }

    public Long getSpawnLong() {
        return spawnLong;
    }

    public String getKey() {
        return key;
    }

    public boolean isInventoryFull(Player player)
    {
        return player.getInventory().firstEmpty() == -1;
    }
}
