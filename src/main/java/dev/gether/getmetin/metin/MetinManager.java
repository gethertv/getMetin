package dev.gether.getmetin.metin;

import dev.gether.getmetin.GetMetin;
import dev.gether.getmetin.data.CmdDrop;
import dev.gether.getmetin.data.FinalRewards;
import dev.gether.getmetin.data.ItemDrop;
import dev.gether.getmetin.data.MetinType;
import dev.gether.getmetin.user.User;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MetinManager {

    private final GetMetin plugin;

    private List<Metin> metinData = new ArrayList<>();
    private HashMap<UUID, User> userAdmin = new HashMap<>();

    public static int SLOT_ADD_ITEM = 47;
    public static int SLOT_ADD_CMD = 49;
    public static int SLOT_SET_FINAL_ITEMS = 51;
    public static int SLOT_ADD = 26;
    public static int SLOT_BACK = 18;
    public MetinManager(GetMetin plugin)
    {
        this.plugin = plugin;
        loadMetins();
    }

    private void loadMetins() {
        for(String key : plugin.getConfig().getConfigurationSection("metin").getKeys(false))
        {
            String name = plugin.getConfig().getString("metin."+key+".name");
            MetinType metinType = MetinType.BLOCK;
            if(plugin.getConfig().isSet("metin."+key+".type"))
            {
                metinType = MetinType.valueOf(plugin.getConfig().getString("metin."+key+".type").toUpperCase());
            }
            EntityType entityType = EntityType.PLAYER;
            if(plugin.getConfig().isSet("metin."+key+".entity-type"))
            {
                entityType = EntityType.valueOf(plugin.getConfig().getString("metin."+key+".entity-type").toUpperCase());
            }
            List<String> hologramList = new ArrayList<>();
            Material material = Material.valueOf(plugin.getConfig().getString("metin."+key+".material").toUpperCase());
            hologramList.addAll(plugin.getConfig().getStringList("metin."+key+".hologram"));
            int hp = plugin.getConfig().getInt("metin."+key+".hp");
            List<CmdDrop> cmdDrop = new ArrayList<>();
            for(String index : plugin.getConfig().getConfigurationSection("metin."+key+".commands").getKeys(false))
            {
                double chance = plugin.getConfig().getDouble("metin."+key+".commands."+index+".chance");
                String cmd = plugin.getConfig().getString("metin."+key+".commands."+index+".cmd");

                cmdDrop.add(new CmdDrop(index, cmd, chance));
            }
            List<ItemDrop> itemDrops = new ArrayList<>();
            for(String index : plugin.getConfig().getConfigurationSection("metin."+key+".items").getKeys(false))
            {
                double chance = plugin.getConfig().getDouble("metin."+key+".items."+index+".chance");
                ItemStack itemStack = plugin.getConfig().getItemStack("metin."+key+".items."+index+".item");

                itemDrops.add(new ItemDrop(index, itemStack, chance));
            }

            FinalRewards finalRewards = loadFinalRewards(key);

            metinData.add(new Metin(metinType, entityType, key, name, hp, material ,hologramList, itemDrops, cmdDrop, finalRewards));
        }
    }

    private FinalRewards loadFinalRewards(String key) {
        List<CmdDrop> cmdDrop = new ArrayList<>();
        for(String index : plugin.getConfig().getConfigurationSection("metin."+key+".final-rewards.commands").getKeys(false))
        {
            double chance = plugin.getConfig().getDouble("metin."+key+".final-rewards.commands."+index+".chance");
            String cmd = plugin.getConfig().getString("metin."+key+".final-rewards.commands."+index+".cmd");

            cmdDrop.add(new CmdDrop(index, cmd, chance));
        }
        List<ItemDrop> itemDrops = new ArrayList<>();
        for(String index : plugin.getConfig().getConfigurationSection("metin."+key+".final-rewards.items").getKeys(false))
        {
            double chance = plugin.getConfig().getDouble("metin."+key+".final-rewards.items."+index+".chance");
            ItemStack itemStack = plugin.getConfig().getItemStack("metin."+key+".final-rewards.items."+index+".item");

            itemDrops.add(new ItemDrop(index, itemStack, chance));
        }
        return new FinalRewards(cmdDrop, itemDrops);
    }

    public void adminEditMetin(Player player, Metin metin)
    {
        userAdmin.put(player.getUniqueId(), new User(player, metin));

    }

    public Metin getMetin(String name)
    {
        for (Metin metin : getMetinData()) {
            if(metin.getName().equalsIgnoreCase(name))
                return metin;
        }
        return null;
    }

    public List<Metin> getMetinData() {
        return metinData;
    }

    public HashMap<UUID, User> getUserAdmin() {
        return userAdmin;
    }
}
