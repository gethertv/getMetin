package dev.gether.getmetin;

import dev.gether.getmetin.cmd.GetMetinCmd;
import dev.gether.getmetin.data.MetinData;
import dev.gether.getmetin.data.MetinType;
import dev.gether.getmetin.file.MetinyFile;
import dev.gether.getmetin.listeners.*;
import dev.gether.getmetin.metin.Metin;
import dev.gether.getmetin.metin.MetinManager;
import dev.gether.getmetin.task.SpawnMetinTask;
import dev.gether.getmetin.utils.ColorFixer;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class GetMetin extends JavaPlugin {

    private static GetMetin instance;

    private MetinManager metinManager;
    private HashMap<Location, MetinData> metinData = new HashMap<>();
    @Override
    public void onEnable() {

        instance = this;
        saveDefaultConfig();
        MetinyFile.loadFile();

        if (getServer().getPluginManager().getPlugin("DecentHolograms") == null) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        metinManager = new MetinManager(this);

        new InventoryClickListener(this);
        new BreakBlockListener(this);
        new PlaceBlockListener(this);
        new InteractClickListener(this);
        new EntityHitListener(this);
        new ConnectListener(this);

        loadMetin();

        new SpawnMetinTask(this).runTaskTimer(this, 20L, 20L);

        new GetMetinCmd(this, "getmetin");
    }



    @Override
    public void onDisable() {

        if (getServer().getPluginManager().getPlugin("DecentHolograms") == null)
            return;

        for(MetinData metin : getMetinData().values())
        {
            if(metin.getHologram()==null) continue;
            metin.getHologram().destroy();
            metin.getMetinLoc().getBlock().setType(Material.AIR);
            if(metin.getMetin().getMetinType()== MetinType.CITIZENS)
            {
                NPC npc = metin.getNpc();
                if(npc!=null)
                    npc.destroy();
            }
        }
        Bukkit.getScheduler().cancelTasks(this);
        HandlerList.unregisterAll(this);

    }
    public void reloadPlugin(Player player)
    {
        reloadConfig();
        MetinyFile.loadFile();
        for(MetinData metin : getMetinData().values())
        {
            if(metin.getHologram()==null) continue;
            metin.getHologram().destroy();
            metin.getMetinLoc().getBlock().setType(Material.AIR);
            if(metin.getMetin().getMetinType()== MetinType.CITIZENS)
            {
                NPC npc = metin.getNpc();
                if(npc!=null)
                    npc.destroy();
            }
        }
        metinData.clear();
        metinManager = new MetinManager(this);
        loadMetin();
        player.sendMessage(ColorFixer.addColors("&aPomyslnie przeladowano plugin!"));
    }
    private void loadMetin() {
        FileConfiguration config =  MetinyFile.getConfig();
        for(String key : config.getConfigurationSection("metin").getKeys(false))
        {
            Location loc =  config.getLocation("metin."+key+".loc");
            int second =  config.getInt("metin."+key+".second");
            String keyMetin = config.getString("metin."+key+".key");
            double heightY = GetMetin.getInstance().getConfig().getDouble("hologram-y");
            if(config.isSet("metin."+key+".height-y"))
                heightY = config.getDouble("metin."+key+".height-y");;

            Metin metin = getMetin(keyMetin);
            if(metin==null)
                continue;


            metinData.put(loc, new MetinData(key, loc, metin, second, heightY));
        }
    }


    public Metin getMetin(String key)
    {
        for (Metin metin : getMetinManager().getMetinData()) {
            if(metin.getKey().equalsIgnoreCase(key))
                return metin;
        }
        return null;
    }
    public MetinManager getMetinManager() {
        return metinManager;
    }

    public HashMap<Location, MetinData> getMetinData() {
        return metinData;
    }

    public static GetMetin getInstance() {
        return instance;
    }

}
