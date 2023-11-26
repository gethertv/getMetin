package dev.gether.getmetin.data;

import dev.gether.getmetin.GetMetin;
import dev.gether.getmetin.metin.Metin;
import dev.gether.getmetin.utils.ColorFixer;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;

public class MetinData {

    private boolean enable;
    private String key;
    private Hologram hologram;
    private int hp;
    private Location metinLoc;
    private Metin metin;

    private Long spawnLong;
    private int second;
    private NPC npc;
    private double heightY;

    private Random random;
    public MetinData(String key, Location metinLoc, Metin metin, int second, double heightY) {
        this.key = key;
        this.metinLoc = metinLoc;
        this.hp = metin.getMaxHp();
        this.second = second;
        this.metin = metin;
        spawnLong = System.currentTimeMillis()+(second*1000L);
        this.heightY = heightY;
        random = new Random();
    }

    public void createMetin() {

        if(metin.getMetinType()==MetinType.CITIZENS
                && npc!=null
                && npc.isSpawned()) {
            npc.destroy();
        }
        if(hologram!=null)
            hologram.destroy();

        enable = true;
        this.hp = metin.getMaxHp();

        spawnLong = System.currentTimeMillis() + (second * 1000L);
        if(metin.getMetinType()==MetinType.BLOCK)
            metinLoc.getBlock().setType(metin.getMaterial());

        if(metin.getMetinType()==MetinType.CITIZENS)
            spawnNpc();

        hologram = DHAPI.createHologram(UUID.randomUUID().toString(), metinLoc.clone().add(0.5, heightY, 0.5), getHolo());

        spawnLong = System.currentTimeMillis() + (second * 1000L);
    }

    public void spawnNpc()
    {
        npc = CitizensAPI.getNPCRegistry().createNPC(metin.getEntityType(), metin.getName());
        String old = "false";
        npc.data().setPersistent(NPC.Metadata.NAMEPLATE_VISIBLE, old);
        npc.spawn(metinLoc.clone().add(0.5, 0, 0.5));
        npc.scheduleUpdate(NPC.NPCUpdate.PACKET);
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
        if(!enable)
            return;

        hp--;
        if(hp<=0)
        {
            enable = false;
            giveFinalReward(player, metinLoc);
            Bukkit.broadcastMessage(ColorFixer.addColors(GetMetin.getInstance().getConfig().getString("lang.metin-down")
                    .replace("{x}", String.valueOf(metinLoc.getBlockX()))
                    .replace("{y}", String.valueOf(metinLoc.getBlockY()))
                    .replace("{z}", String.valueOf(metinLoc.getBlockZ()))
            ));

            if(hologram!=null)
                hologram.destroy();

            if(metin.getMetinType()==MetinType.BLOCK)
                metinLoc.getBlock().setType(Material.AIR);
            if(metin.getMetinType()==MetinType.CITIZENS)
                if(npc!=null)
                    npc.destroy();


            return;
        }
        giveReward(player, metinLoc);
        updateHolo();

    }

    public NPC getNpc() {
        return npc;
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
